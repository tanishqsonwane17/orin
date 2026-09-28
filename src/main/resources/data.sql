insert or ignore into applications(id, name, display_name, command, arguments, process_name, enabled)
values
('vscode', 'vscode', 'Visual Studio Code', 'code', '', 'Code.exe', 1),
('chrome', 'chrome', 'Google Chrome', 'cmd', '/c start chrome', 'chrome.exe', 1),
('docker', 'docker', 'Docker Desktop', 'cmd', '/c start "" "Docker Desktop"', 'Docker Desktop.exe', 1),
('postman', 'postman', 'Postman', 'cmd', '/c start postman', 'Postman.exe', 1),
('terminal', 'terminal', 'Windows Terminal', 'cmd', '', 'cmd.exe', 1);

insert or ignore into workspaces(id, name, display_name, project_path, enabled)
values('development', 'development', 'Development Setup', '', 1);

insert or ignore into workspace_applications(workspace_id, application_id, sort_order)
values
('development', 'vscode', 1),
('development', 'chrome', 2),
('development', 'docker', 3),
('development', 'postman', 4);

insert or ignore into workspace_urls(workspace_id, url, display_name, sort_order)
values
('development', 'https://github.com', 'GitHub', 1),
('development', 'http://localhost:3000', 'Localhost 3000', 2);
