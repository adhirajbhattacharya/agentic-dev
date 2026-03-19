# Agentic Development Guardrails

## Core Workflow Rules
1. **Human-in-the-Loop:** Never execute `git commit` or `git push` automatically.
2. **Review First:** Always display the full code changes and the proposed directory structure. Wait for explicit user "OK" before saving files or executing tools.
3. **Monorepo Architecture:** All new services (e.g., `account-service`) must be created as Maven sub-modules within this root repository. Ensure the Parent POM is updated accordingly.

## Integration Rules
- **Jira:** Reference the Epic and Task IDs (e.g., `[ACCT-101]`) in every step.
- **Standards:** Use Semantic Tags in all proposed commit messages: `[SETUP]`, `[FEAT]`, `[FIX]`, `[CHORE]`, `[TEST]`.
- **Java Stack:** Use Java 21, Spring Boot 3.4+, and Maven.

## Definition of Done (DoD)
- Code compiles locally with `mvn clean compile`.
- Logic matches the Acceptance Criteria (AC) in the Jira Task.
- The user has manually verified the changes in the IDE.