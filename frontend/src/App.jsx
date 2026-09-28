import { useCallback, useEffect, useState } from "react";
import BrandHeader from "./components/BrandHeader";
import ChatComposer from "./components/ChatComposer";
import ConnectionStatus from "./components/ConnectionStatus";
import ResponsePanel from "./components/ResponsePanel";
import {
  getAssistantHealth,
  sendAssistantMessage,
} from "./services/assistantApi";

export default function App() {
  const [connection, setConnection] = useState("checking");
  const [message, setMessage] = useState("");
  const [response, setResponse] = useState(null);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const checkConnection = useCallback(async () => {
    setConnection("checking");
    try {
      await getAssistantHealth();
      setConnection("online");
    } catch {
      setConnection("offline");
    }
  }, []);

  useEffect(() => {
    checkConnection();
  }, [checkConnection]);

  async function handleSubmit() {
    const trimmedMessage = message.trim();
    if (!trimmedMessage || loading) return;

    setLoading(true);
    setError("");

    try {
      const data = await sendAssistantMessage(trimmedMessage);
      setResponse(data);
      setMessage("");
      setConnection("online");
    } catch (requestError) {
      setError(requestError.message);
      setConnection("offline");
    } finally {
      setLoading(false);
    }
  }

  function handleMicrophone() {
    setError("Voice input is reserved for the next Orin milestone.");
  }

  return (
    <main className="app-shell">
      <div className="ambient ambient--one" />
      <div className="ambient ambient--two" />

      <section className="assistant-card">
        <div className="assistant-card__topline">
          <span>DESKTOP ASSISTANT / MVP 01</span>
          <ConnectionStatus status={connection} onRetry={checkConnection} />
        </div>

        <BrandHeader />

        <div className="conversation">
          <ChatComposer
            message={message}
            onMessageChange={setMessage}
            onSubmit={handleSubmit}
            onMicrophone={handleMicrophone}
            loading={loading}
            disabled={connection !== "online"}
          />
          <ResponsePanel response={response} error={error} loading={loading} />
        </div>

        <footer>
          <span>Built by OmniTrixs</span>
          <span className="footer-separator" />
          <span>Private by design</span>
        </footer>
      </section>
    </main>
  );
}
