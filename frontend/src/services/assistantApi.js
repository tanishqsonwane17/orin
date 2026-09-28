const API_URL = (import.meta.env.VITE_API_URL || "http://localhost:8080").replace(/\/$/, "");

async function request(path, options) {
  let response;

  try {
    response = await fetch(`${API_URL}${path}`, options);
  } catch {
    throw new Error("Cannot reach the Orin service. Make sure the Java backend is running.");
  }

  const data = await response.json().catch(() => null);

  if (!response.ok) {
    throw new Error(data?.message || "Orin could not complete the request.");
  }

  return data;
}

export function getAssistantHealth() {
  return request("/api/assistant/health");
}

export function sendAssistantMessage(message) {
  return request("/api/assistant/message", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({ message }),
  });
}
