# Security Policy

## Supported versions

Only the latest released version of RuleScribe receives security fixes.
The plugin is distributed through the JetBrains Marketplace and as GitHub
release artifacts; always run the most recent build.

| Version | Supported |
| ------- | --------- |
| latest  | ✅        |
| older   | ❌        |

## Reporting a vulnerability

Please report suspected vulnerabilities **privately**, not through public
issues:

1. Preferred: open a private advisory via GitHub Security Advisories
   (repository *Security* tab → *Report a vulnerability*).

Please include:

- the affected version,
- a description of the issue and its impact,
- reproduction steps or a proof of concept if available.

You can expect an initial acknowledgement within **7 days**. Once a fix is
released, credit will be given to the reporter unless anonymity is
requested.

## Security posture

RuleScribe is intentionally low-risk by construction:

- **No third-party runtime dependencies are bundled.** The plugin declares
  only a single test-scoped dependency (JUnit); the Kotlin standard library
  and all UI/PSI APIs are provided by the host IntelliJ Platform at runtime.
- **No network access.** The plugin performs no outbound network calls; it
  only reads and analyzes `.rules` files in the open project.
- **No code execution.** It parses and inspects DSL text; it does not
  execute rules or any user-supplied code.

## Automated scanning

Every push and pull request to `main` runs, in GitHub Actions:

- **CodeQL** static analysis (`java-kotlin`) for code-level vulnerabilities,
  also on a weekly schedule (`.github/workflows/codeql.yml`).
- **OWASP Dependency-Check** against the NVD database, scoped to the plugin's
  **shipped** runtime classpath; the build fails on any bundled dependency with
  a CVSS score ≥ 7.0 (`.github/workflows/build.yml`). Build- and test-time
  dependencies (JUnit and the IntelliJ Platform SDK) are **not** distributed in
  the plugin and are out of scope; platform libraries are patched by JetBrains
  through IDE updates.
- **Dependabot** keeps Gradle dependencies and GitHub Actions up to date
  (`.github/dependabot.yml`).
