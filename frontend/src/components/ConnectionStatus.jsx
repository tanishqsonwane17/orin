const labels = {
  checking: "Connecting",
  online: "System online",
  offline: "Service offline",
};

export default function ConnectionStatus({ status, onRetry }) {
  const content = (
    <>
      <span className="status-indicator__dot" aria-hidden="true" />
      <span>{labels[status]}</span>
    </>
  );

  if (status === "offline") {
    return (
      <button className={`status-indicator status-indicator--${status}`} onClick={onRetry} type="button">
        {content}
        <span className="status-indicator__retry">Retry</span>
      </button>
    );
  }

  return (
    <div className={`status-indicator status-indicator--${status}`} role="status">
      {content}
    </div>
  );
}
