export default function ChatComposer({ message, onMessageChange, onSubmit, onMicrophone, loading, disabled }) {
  function handleKeyDown(event) {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      onSubmit();
    }
  }

  return (
    <div className="composer-wrap">
      <label className="sr-only" htmlFor="orin-message">
        Message Orin
      </label>
      <textarea
        id="orin-message"
        value={message}
        onChange={(event) => onMessageChange(event.target.value)}
        onKeyDown={handleKeyDown}
        placeholder="Ask Orin anything..."
        rows="1"
        maxLength="1000"
        disabled={loading}
      />
      <div className="composer-actions">
        <button
          className="icon-button"
          type="button"
          onClick={onMicrophone}
          aria-label="Use microphone"
          title="Voice input coming soon"
        >
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="M12 15.5a3.5 3.5 0 0 0 3.5-3.5V6a3.5 3.5 0 1 0-7 0v6a3.5 3.5 0 0 0 3.5 3.5Z" />
            <path d="M5.5 11.5v.5a6.5 6.5 0 0 0 13 0v-.5M12 18.5V22M9 22h6" />
          </svg>
        </button>
        <button
          className="send-button"
          type="button"
          onClick={onSubmit}
          disabled={disabled || loading || !message.trim()}
        >
          {loading ? "Sending" : "Send"}
          <svg viewBox="0 0 24 24" aria-hidden="true">
            <path d="m5 12 14-7-4.5 14-2.7-5.1L5 12Z" />
            <path d="m11.8 13.9 3.4-4" />
          </svg>
        </button>
      </div>
    </div>
  );
}
