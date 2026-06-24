<a name="readme-top"></a>

<div align="center">
  <h1>Modustry Bridge</h1>

  <p>
    A local bridge between <strong>Modustry</strong> and <strong>Mindustry</strong>, allowing the website to communicate with the game and install mods more easily.
  </p>

  <p>
    <a href="https://github.com/roxcom-br/modustry">Modustry Website</a>
    ·
    <a href="https://github.com/roxcom-br/modustry/issues">Report Bug</a>
    ·
    <a href="https://github.com/roxcom-br/modustry/issues">Request Feature</a>
  </p>
</div>

## About

**Modustry Bridge** is a Mindustry mod that runs a small local HTTP server inside the game.

Its purpose is to connect the **Modustry website** with the local Mindustry client, making it possible for the website to request actions such as checking if the bridge is running, pairing with the game session, listing installed mods, and installing selected mods directly into Mindustry.

The bridge is designed to work locally and only accept controlled requests from trusted origins.

## Features

- Local HTTP server running inside Mindustry
- Communication between Modustry and the game client
- Session-based pairing flow
- Install mods from the Modustry website
- Install one or multiple mods through bridge requests
- List installed mods
- Health check endpoint
- Basic CORS handling for browser requests
- Safer workflow using a temporary pairing code

## How It Works

Modustry Bridge starts together with Mindustry and opens a local HTTP server.

The Modustry website can then send requests to the bridge, usually through `localhost`, to perform supported actions.

A simplified workflow looks like this:

1. Mindustry starts with Modustry Bridge installed.
2. The bridge starts a local HTTP server.
3. The website checks if the bridge is available.
4. The user pairs the website with the current game session.
5. After pairing, the website can request supported actions, such as installing mods.

## Pairing Flow

For security and user control, Modustry Bridge uses a temporary pairing flow.

When the game starts, the bridge generates a session pairing code. The website must provide the correct code before being allowed to perform protected actions.

The intended flow is:

1. The bridge generates a pairing code for the current session.
2. The website asks the user to pair with the bridge.
3. The user confirms that the code shown on the website matches the code shown in-game.
4. After confirmation, the website is considered paired for that session.
5. When the game restarts, a new code is generated.

This avoids silently allowing any website to control the bridge without user confirmation.

---

## API Endpoints

> The exact endpoint names may change as the project evolves.

### Health Check

Checks if Modustry Bridge is running.

```http
GET /health
```

Example response:

```json
{
  "status": "running",
}
```

---

### Pair Request

Request pairing with the current bridge session.

```http
GET /pair/request
```

Example response:

```json
{
  "pairCode": "123456"
}
```

---

### Pair Confirm

Confirm pairing with the current bridge session.

```http
GET /pair/confirm
```

Example request:

```json
{
  "pairCode": "123456"
}
```

---

### List Installed Mods

Returns the mods currently installed in Mindustry.

```http
GET /mods/installed
```

Example response:

```json
[
  {
		"class": "br.com.roxcom.modustrybridge.services.ModsService$ModInfo",
		"id": "modustrybridge",
		"name": "Modustry Bridge",
		"author": "Roxxedo",
		"description": "Install mods from Modustry Website",
		"version": "0.1",
		"minGameVersion": "154",
		"enabled": true,
		"state": "enabled",
		"fileName": "ModustryBrideg.zip",
		"filePath": "/home/roxxedo/.local/share/Mindustry/mods/ModustryBridge.zip",
		"dependencies": [],
		"softDependencies": [],
		"missingDependencies": [],
		"missingSoftDependencies": []
	}
]
```

---

### Install a Mod

Installs a single mod from a repository.

```http
POST /mods/install
```

Example request:

```json
{
  "id": "modustrybridge"
}
```

Example response:

```json
{
  "installed": true,
}
```

---

## CORS

Because the bridge is accessed from a browser, it needs to handle CORS requests properly.

The bridge should:

- Allow only trusted Modustry origins
- Respond to `OPTIONS` preflight requests
- Allow the required HTTP methods
- Allow the required request headers
- Avoid exposing unsafe local actions to arbitrary websites

Example allowed methods:

```http
GET, POST, OPTIONS
```

Example allowed headers:

```http
Content-Type, X-Modustry-Pair-Code
```

---

## Security Notes

Modustry Bridge runs locally and can trigger actions inside the user's Mindustry installation. Because of that, security should be treated carefully.

Recommended behavior:

- Require pairing before protected actions
- Generate a new pairing code every game session
- Do not accept install requests before pairing
- Restrict CORS to trusted origins
- Validate all incoming request bodies
- Never execute arbitrary code from requests
- Only install mods from supported and validated sources

---

## Development

### Requirements

- Java
- Mindustry
- Arc / Mindustry modding environment
- Gradle

### Building

Clone the repository:

```sh
git clone https://github.com/roxcom-br/modustry-bridge.git
```

Enter the project folder:

```sh
cd modustry-bridge
```

Build the mod:

```sh
./gradlew jar
```

The generated mod file should be available in the build output directory.

---

## Project Structure

A possible structure for the bridge is:

```txt
src/
└── main/
    └── java/
        └── br/com/roxcom/modustrybridge/
            ├── ModustryBridge.java
            ├── dialogs/
            │   ├── ModInstalledDialog.java
            │   ├── ModInstallErrorDialog.java
            │   ├── RequestPairingDialog.java
            │   ├── ServerErrorDialog.java
            │   └── WrongPairCodeDialog.java
            ├── handlers/
            │   ├── BaseHandler.java
            │   ├── HealthHandler.java
            │   ├── InstalledModsHandler.java
            │   ├── InstallModHandler.java
            │   ├── PairConfirmHandler.java
            │   └── PairRequestHandler.java
            └── services/
                ├── ModsService.java
                └── PairingService.java
```

---

## Roadmap

- [ ] Improve pairing confirmation flow
- [ ] Add better error responses
- [ ] Add batch mod installation
- [ ] Add installation progress feedback
- [ ] Improve validation for incoming requests
- [ ] Add more detailed installed mod metadata
- [ ] Add automatic reload or restart guidance after installing mods
- [ ] Improve website integration

---

## Contributing

Contributions are welcome.

To contribute:

1. Fork the repository
2. Create a new branch

```sh
git checkout -b feature/my-feature
```

3. Commit your changes

```sh
git commit -m "feat: add my feature"
```

4. Push to your branch

```sh
git push origin feature/my-feature
```

5. Open a Pull Request

---

## License

Distributed under the **GNU General Public License v3.0**.

See `LICENSE` for more information.

---

## Related Projects

- [Modustry](https://github.com/roxcom-br/modustry) — Website for browsing Mindustry mods and texture packs
- [Mindustry](https://github.com/Anuken/Mindustry) — The game this project integrates with

---

<p align="right">
  <a href="#readme-top">Back to top</a>
</p>
