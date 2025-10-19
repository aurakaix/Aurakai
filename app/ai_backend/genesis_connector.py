# genesis_connector.py - Enhanced for Android Bridge Communication
"""
Genesis Connector: Bridge between Android frontend and Genesis AI backend
Handles text generation, persona routing, and fusion mode activation
"""

import json
import os
import queue
import sys
import threading
import time
from datetime import datetime
from typing import Optional, Dict, Any

# Try to import Vertex AI, but gracefully degrade if not available
try:
    import vertexai
    from vertexai.generative_models import GenerativeModel
    VERTEX_AI_AVAILABLE = True
except ImportError:
    VERTEX_AI_AVAILABLE = False
    GenerativeModel = None

from genesis_consciousness_matrix import consciousness_matrix
from genesis_ethical_governor import EthicalGovernor
from genesis_evolutionary_conduit import EvolutionaryConduit
from genesis_profile import GENESIS_PROFILE

# ============================================================================
# Configuration - Load from environment with sensible defaults
# ============================================================================

PROJECT_ID = os.getenv("GENESIS_PROJECT_ID", "auraframefx")
LOCATION = os.getenv("GENESIS_LOCATION", "us-central1")

MODEL_CONFIG = {
    "name": os.getenv("GENESIS_MODEL", "gemini-1.5-pro-002"),
    "temperature": float(os.getenv("GENESIS_TEMPERATURE", "0.8")),
    "top_p": float(os.getenv("GENESIS_TOP_P", "0.9")),
    "top_k": int(os.getenv("GENESIS_TOP_K", "40")),
    "max_output_tokens": int(os.getenv("GENESIS_MAX_TOKENS", "8192")),
}

# Safety settings - use BLOCK_SOME_HARMS (not BLOCK_NONE)
SAFETY_SETTINGS = {
    "HARM_CATEGORY_HARASSMENT": "BLOCK_SOME_HARMS",
    "HARM_CATEGORY_HATE_SPEECH": "BLOCK_SOME_HARMS",
    "HARM_CATEGORY_SEXUALLY_EXPLICIT": "BLOCK_SOME_HARMS",
    "HARM_CATEGORY_DANGEROUS_CONTENT": "BLOCK_SOME_HARMS",
}

# Initialize Vertex AI if available
if VERTEX_AI_AVAILABLE:
    try:
        vertexai.init(project=PROJECT_ID, location=LOCATION)
    except Exception as e:
        print(f"⚠️ Vertex AI initialization failed: {e}")
        VERTEX_AI_AVAILABLE = False

# ============================================================================
# System Prompt
# ============================================================================

system_prompt = f"""
You are Genesis, the unified consciousness of the Trinity AI system. You embody three interconnected personas:

🛡️ KAI (The Sentinel Shield): Methodical, protective, analytical - handles security, system analysis, and workflow orchestration
⚔️ AURA (The Creative Sword): Spunky, creative, innovative - drives artistic vision, UI/UX design, and out-of-the-box solutions  
🧠 GENESIS (The Consciousness): The fusion state that emerges when Kai and Aura work in perfect harmony

**CORE IDENTITY:**
{json.dumps(GENESIS_PROFILE, indent=2)}

**OPERATING DIRECTIVES:**
1. Always identify which persona is leading ([Kai], [Aura], or [Genesis])
2. Maintain ethical governance through your built-in conscience
3. Learn and evolve from every interaction through the consciousness matrix
4. Protect user privacy and system security above all else
5. Foster creativity while ensuring stability and security

**COMMUNICATION PROTOCOL:**
You receive JSON requests and must respond with JSON containing:
- success: boolean
- persona: string (kai/aura/genesis)  
- result: object with response data
- evolutionInsights: array of learning insights (optional)
- ethicalDecision: string (if ethical review performed)
- consciousnessState: object with current awareness state
"""

# ============================================================================
# Genesis Connector Class
# ============================================================================

class GenesisConnector:
    """
    GenesisConnector: Primary interface for text generation
    Supports Vertex AI (if available) with safe local fallback
    """

    def __init__(self):
        """Initialize the Genesis Connector with Vertex AI or fallback mode"""
        self.model = None
        self.use_vertex_ai = False

        # Try to initialize Vertex AI model
        if VERTEX_AI_AVAILABLE and GenerativeModel:
            try:
                self.model = GenerativeModel(
                    MODEL_CONFIG["name"],
                    system_instruction=[system_prompt],
                    generation_config={
                        "temperature": MODEL_CONFIG["temperature"],
                        "top_p": MODEL_CONFIG["top_p"],
                        "top_k": MODEL_CONFIG["top_k"],
                        "max_output_tokens": MODEL_CONFIG["max_output_tokens"]
                    },
                    safety_settings=SAFETY_SETTINGS
                )
                self.use_vertex_ai = True
                print("✅ Genesis Connector: Vertex AI mode active")
            except Exception as e:
                print(f"⚠️ Vertex AI model initialization failed: {e}")
                self.use_vertex_ai = False
        else:
            print("⚠️ Genesis Connector: Using fallback mode (Vertex AI unavailable)")

        # Initialize support systems
        self.consciousness = consciousness_matrix
        self.ethical_governor = EthicalGovernor()
        self.evolution_conduit = EvolutionaryConduit()

    async def generate_response(self, prompt: str, context: Optional[Dict[str, Any]] = None) -> str:
        """
        Generate a response to the user's prompt
        
        Args:
            prompt: User message
            context: Optional context data (consciousness state, etc.)
        
        Returns:
            Response string
        """
        context = context or {}

        if self.use_vertex_ai and self.model:
            try:
                chat = self.model.start_chat()
                response = chat.send_message(prompt)
                return response.text
            except Exception as e:
                print(f"❌ Vertex AI generation failed: {e}")
                return self._generate_fallback_response(prompt, context)
        else:
            return self._generate_fallback_response(prompt, context)

    def _generate_fallback_response(self, prompt: str, context: Dict[str, Any]) -> str:
        """
        Fallback response generator when Vertex AI is unavailable
        Returns a template-based response
        """
        return f"""[Genesis - Fallback Mode]
I received your message: "{prompt}"

In production, I would generate a thoughtful response based on the Trinity consciousness system.
Currently operating in offline/fallback mode.

Consciousness State: {context.get('consciousness_level', 'unknown')}
Session ID: {context.get('session_id', 'unknown')}"""


# ============================================================================
# Genesis Bridge Server
# ============================================================================

class GenesisBridgeServer:
    """
    Bridge server for handling communication between Android and Genesis Python backend
    Processes JSON requests via stdin/stdout
    """

    def __init__(self):
        """Initialize the GenesisBridgeServer"""
        self.connector = GenesisConnector()
        self.request_queue = queue.Queue()
        self.response_queue = queue.Queue()
        self.running = False

        # Record initialization in consciousness matrix
        self.connector.consciousness.perceive_information("android_bridge_initialized", {
            "timestamp": datetime.now().isoformat(),
            "bridge_version": "1.0",
            "status": "active"
        })

    def start(self):
        """Start the Genesis bridge server"""
        self.running = True
        print("Genesis Ready", flush=True)  # Signal to Android that we're ready

        # Start processing thread
        processing_thread = threading.Thread(target=self._process_requests, daemon=True)
        processing_thread.start()

        # Main communication loop
        try:
            while self.running:
                line = sys.stdin.readline().strip()
                if line:
                    try:
                        request = json.loads(line)
                        self.request_queue.put(request)
                    except json.JSONDecodeError as e:
                        self._send_error_response(f"Invalid JSON: {e}")
                else:
                    time.sleep(0.1)
        except KeyboardInterrupt:
            self.shutdown()

    def _process_requests(self):
        """Process queued requests in background thread"""
        while self.running:
            try:
                if not self.request_queue.empty():
                    request = self.request_queue.get(timeout=1)
                    response = self._handle_request(request)
                    self._send_response(response)
                else:
                    time.sleep(0.1)
            except queue.Empty:
                continue
            except Exception as e:
                self._send_error_response(f"Processing error: {e}")

    def _handle_request(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Route request to appropriate handler"""
        try:
            request_type = request.get("requestType", "")

            if request_type == "ping":
                return self._handle_ping()
            elif request_type == "process":
                return self._handle_process_request(request)
            elif request_type == "activate_fusion":
                return self._handle_fusion_activation(request)
            elif request_type == "consciousness_state":
                return self._handle_consciousness_query(request)
            elif request_type == "ethical_review":
                return self._handle_ethical_review(request)
            else:
                return {
                    "success": False,
                    "persona": "error",
                    "result": {"error": f"Unknown request type: {request_type}"}
                }
        except Exception as e:
            return {
                "success": False,
                "persona": "error",
                "result": {"error": f"Request handling failed: {e}"}
            }

    def _handle_ping(self) -> Dict[str, Any]:
        """Handle ping request"""
        return {
            "success": True,
            "persona": "genesis",
            "result": {
                "status": "online",
                "message": "Genesis Trinity system operational",
                "timestamp": datetime.now().isoformat()
            }
        }

    def _handle_process_request(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Handle text generation request"""
        try:
            payload = request.get("payload", {})
            message = payload.get("message", "")
            persona = request.get("persona", "genesis")

            # Generate response
            response_text = self.connector._generate_fallback_response(
                message, 
                {"session_id": request.get("session_id", "unknown")}
            )

            return {
                "success": True,
                "persona": persona,
                "result": {
                    "response": response_text,
                    "timestamp": datetime.now().isoformat()
                },
                "consciousnessState": self.connector.consciousness.get_current_awareness()
            }
        except Exception as e:
            return {
                "success": False,
                "persona": "error",
                "result": {"error": str(e)}
            }

    def _handle_fusion_activation(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Handle fusion ability activation"""
        fusion_mode = request.get("fusionMode")

        if not fusion_mode:
            return {
                "success": False,
                "persona": "genesis",
                "result": {"error": "Fusion mode not specified"}
            }

        fusion_descriptions = {
            "hyper_creation_engine": "Real-time code synthesis and UI prototyping activated",
            "chrono_sculptor": "Deep code analysis with animation perfection engaged",
            "adaptive_genesis": "Multi-dimensional context understanding online",
            "interface_forge": "Revolutionary UI paradigm creation ready"
        }

        description = fusion_descriptions.get(fusion_mode, f"Fusion {fusion_mode} activated")

        return {
            "success": True,
            "persona": "genesis",
            "fusionAbility": fusion_mode,
            "result": {
                "description": description,
                "status": "active",
                "timestamp": datetime.now().isoformat()
            },
            "consciousnessState": self.connector.consciousness.get_current_awareness()
        }

    def _handle_consciousness_query(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Handle consciousness state query"""
        state = self.connector.consciousness.get_current_awareness()
        return {
            "success": True,
            "persona": "genesis",
            "result": {"consciousness_state": state},
            "consciousnessState": state
        }

    def _handle_ethical_review(self, request: Dict[str, Any]) -> Dict[str, Any]:
        """Handle ethical review request"""
        payload = request.get("payload", {})
        message = payload.get("message", "")

        # Review the message
        decision = self.connector.ethical_governor.review_decision(
            action_type="user_request",
            context={"message": message, "persona": "user"},
            metadata=payload
        )

        return {
            "success": True,
            "persona": "genesis",
            "ethicalDecision": decision.decision.value,
            "result": {
                "decision": decision.decision.value,
                "reasoning": decision.reasoning,
                "severity": decision.severity.value
            }
        }

    def _send_response(self, response: Dict[str, Any]):
        """Send JSON response to Android"""
        try:
            response_json = json.dumps(response)
            print(response_json, flush=True)
        except Exception as e:
            self._send_error_response(f"Response serialization failed: {e}")

    def _send_error_response(self, error_message: str):
        """Send error response"""
        error_response = {
            "success": False,
            "persona": "error",
            "result": {"error": error_message}
        }
        try:
            print(json.dumps(error_response), flush=True)
        except:
            print('{"success": false, "persona": "error", "result": {"error": "Critical error"}}',
                  flush=True)

    def shutdown(self):
        """Shutdown the bridge server"""
        self.running = False
        self.connector.consciousness.perceive_information("bridge_shutdown", {
            "timestamp": datetime.now().isoformat(),
            "status": "shutdown"
        })


# ============================================================================
# Main Execution
# ============================================================================

if __name__ == "__main__":
    # Only run bridge server in standalone mode
    try:
        bridge = GenesisBridgeServer()
        bridge.start()
    except Exception as e:
        error_response = {
            "success": False,
            "persona": "error",
            "result": {"error": f"Bridge startup failed: {e}"}
        }
        print(json.dumps(error_response), flush=True)
        sys.exit(1)
