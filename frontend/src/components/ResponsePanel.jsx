export default function ResponsePanel({ response, error, loading }) {
  return (
    <section className="response-panel" aria-live="polite" aria-busy={loading}>
      <div className="response-panel__label">
        <span>ORIN RESPONSE</span>
        {response?.timestamp && (
          <time dateTime={response.timestamp}>
            {new Date(response.timestamp).toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" })}
          </time>
        )}
      </div>

      {loading && (
        <div className="response-state response-state--loading">
          <span />
          <span />
          <span />
        </div>
      )}

      {!loading && error && <p className="response-state response-state--error">{error}</p>}

      {!loading && !error && response && (
        <div className="response-content">
          <div className="response-avatar" aria-hidden="true">O</div>
          <p>{response.message}</p>
        </div>
      )}

      {!loading && !error && !response && (
        <p className="response-state response-state--empty">
          Ready when you are. Send a message to begin.
        </p>
      )}
    </section>
  );
}
