# JARVIS

JARVIS is a CLI-first local desktop assistant for Windows. It accepts natural-language commands, routes them through a core `JarvisAgent`, and executes only registered safe tools such as opening configured applications, opening URLs, web search, music search, and workspace setup.

## Architecture

```text
Terminal
  |
  v
CLI layer
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

Java 21, Spring Boot 3, Spring Shell dependency, Gradle, LangChain4j dependency, SQLite, Playwright Java, SLF4J/Logback, JUnit 5, Mockito.

## Prerequisites

- JDK 21
- Gradle, or use the generated project with your preferred Gradle wrapper
- Windows command aliases for configured applications, such as `code` for VS Code
- Optional: `OPENAI_API_KEY` for future LLM-backed routing

## Running JARVIS

```powershell
gradle bootRun
```

The CLI starts with:

```text
J A R V I S

JARVIS is ready.

You >
```

Type `exit` or `quit` to stop.

## Installation

Build the executable jar:

```powershell
gradle bootJar
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
gradle test
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
