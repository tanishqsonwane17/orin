# JARVIS

Orin (built on the existing JARVIS core) is a local desktop assistant foundation for Windows. It includes the original CLI/tool architecture plus a React interface connected through a small Spring Boot REST API.

## Architecture

```text
React UI / Terminal
  |
  v
REST API / CLI layer
  |
  v
JarvisAgent
  |
  v
ToolRegistry
  |
  +-- OS tools
  +-- Browser tools
  +-- Workspace tools
  +-- SQLite repositories
```

The agent core does not depend on the CLI. Future voice or GUI inputs can call `JarvisAgent.process(String)` directly.

## Tech Stack

Java 21, Spring Boot 3, React, Vite, Spring Shell dependency, Gradle, LangChain4j dependency, SQLite, Playwright Java, SLF4J/Logback, JUnit 5, Mockito.

## Prerequisites

- JDK 21
- Node.js 18 or newer
- Windows command aliases for configured applications, such as `code` for VS Code
- Optional: `OPENAI_API_KEY` for future LLM-backed routing

## Running Orin

Start the Java API from the project root:

```powershell
$env:JAVA_HOME="C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot"
.\gradlew.bat bootRun
```

In a second terminal, start the React UI:

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`.

The frontend reads its backend URL from `VITE_API_URL`. Copy `frontend/.env.example` to `frontend/.env` when a non-default URL is needed.

### REST API

```text
GET  /api/assistant/health
POST /api/assistant/message
```

The message endpoint currently returns a safe placeholder response and does not invoke AI or desktop automation.

### Legacy CLI mode

The existing CLI remains available:

```powershell
$env:JARVIS_CLI_ENABLED="true"
.\gradlew.bat bootRun
```

Type `exit` or `quit` to stop the CLI.

## Installation

Build the executable jar:

```powershell
.\gradlew.bat bootJar
```

Create a small `jarvis.cmd` somewhere on your `PATH`:

```cmd
@echo off
java -jar C:\path\to\jarvis.jar %*
```

For a native Windows package, use `jpackage` after building the jar:

```powershell
jpackage --type app-image --name jarvis --input build\libs --main-jar jarvis.jar --dest build\package
```

Add the generated package directory, or a wrapper script pointing to it, to `PATH`.

## Configuration

Configuration lives in `src/main/resources/application.yml`.

```yaml
jarvis:
  ai:
    provider: openai
    model: gpt-4.1-mini
  browser:
    enabled: true
  workspace:
    default: development
```

API keys must come from environment variables:

```powershell
$env:OPENAI_API_KEY="..."
```

The MVP includes the core LangChain4j dependency and configuration types. Command routing currently uses a deterministic safe router so the CLI works without an API key; the `AIProvider` boundary is where full LLM tool calling should be enabled next.

## Adding Applications

Applications are stored in SQLite. The initial seed includes:

- `vscode` -> `code`
- `chrome` -> `cmd /c start chrome`
- `docker` -> `Docker Desktop`
- `postman` -> `Postman`
- `terminal` -> `cmd`

Add rows to the `applications` table:

```sql
insert into applications(id, name, display_name, command, arguments, process_name, enabled)
values('idea', 'idea', 'IntelliJ IDEA', 'idea64', '', 'idea64.exe', 1);
```

## Creating Workspaces

Workspaces use:

- `workspaces`
- `workspace_applications`
- `workspace_urls`

Example:

```sql
insert into workspaces(id, name, display_name, project_path, enabled)
values('design', 'design', 'Design Workspace', 'C:/Projects/design', 1);

insert into workspace_applications(workspace_id, application_id, sort_order)
values('design', 'vscode', 1);

insert into workspace_urls(workspace_id, url, display_name, sort_order)
values('design', 'https://github.com', 'GitHub', 1);
```

Then run:

```text
You > configure design workspace
```

## Adding New Tools

Implement `JarvisTool`, give it a unique name, description, permission level, and execution logic, then expose it as a Spring bean. `ToolRegistry` auto-registers all beans.

## Browser Configuration

Browser tools use Playwright Java and open Chromium pages. For the MVP, URL/search/music tools open result pages instead of acting as a full autonomous browser agent.

## Testing

```powershell
.\gradlew.bat test
```

OS launch behavior is mocked in unit tests. Tests must not open real desktop applications.

## Troubleshooting

- If `open vscode` fails, confirm `code` works in a normal terminal.
- If Chrome does not open, update the `chrome` command in SQLite for your machine.
- If the database is corrupt, delete `%USERPROFILE%\.jarvis\jarvis.db` and restart.
- Technical details go to logs; CLI output stays user-friendly.

## AI Search Crawlers

The repository includes `robots.txt` for Automis search visibility controls. Publish it at the website root:

```text
https://automis.example/robots.txt
```

It allows `OAI-SearchBot` for ChatGPT Search discovery while blocking `GPTBot` training crawl. OpenAI documents these crawler controls as independent.
## Future Roadmap

- Full LangChain4j tool-calling agent with confirmation flows
- Voice input and output
- JavaFX or desktop GUI
- Rich workspace editor
- Additional safe desktop tools

Voice, wake word, JavaFX, computer vision, remote servers, Kafka, and microservices are intentionally not implemented in this MVP.
