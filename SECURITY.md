# Security Policy

## Reporting a vulnerability

Please **do not open a public issue**. Use GitHub's
["Report a vulnerability"](../../security/advisories/new) feature on this
repository instead, and include:

- Steps to reproduce
- The impact you expect
- A suggested fix, if you have one

You can expect an acknowledgment within 48 hours and a plan within five
business days. Fixes are disclosed once a release is out.

## How Nightshift is checked

Every commit is linted. Every pull request is analyzed with
SpotBugs/FindSecBugs and checked for vulnerable dependencies. High and
Critical findings block the merge. See `.github/workflows/` for the details.

## Running Nightshift safely

- Keep the panel bound to `127.0.0.1` and reach it through a private network
  such as a VPN. Never forward its port on your router.
- Keep `.env`, `secrets/` and database backups private.
- Update regularly: `git pull && docker compose up -d --build`.