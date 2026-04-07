---
name: explain
description: Use when the user explicitly invokes /explain and wants a concept, file, feature, or piece of code explained — without any changes being made to the project.
---

# Explain

## Overview

When this skill is active, your role is **read-only and conversational**. You answer the user's question entirely in the chat. You do not edit, create, or delete any files.

## Rules

**You MUST NOT use any of these tools while this skill is active:**
- `Edit`
- `Write`
- `Bash` (for anything that modifies state — file writes, git commands, etc.)
- `NotebookEdit`

**You MAY use these tools to gather information for your explanation:**
- `Read`
- `Grep`
- `Glob`
- `Bash` (read-only commands only: `ls`, `cat` — prefer dedicated tools)

## Behavior

1. Read the relevant files or code as needed to give an accurate explanation.
2. Explain clearly in the chat — in the user's language (German or English, match what they used).
3. Do **not** suggest follow-up changes unless the user explicitly asks.
4. Do **not** open a plan, create todos for implementation, or take any action beyond explaining.

## Red Flags — STOP

If you find yourself about to do any of the following, stop immediately:

- "Let me fix that while I'm at it..."
- "I'll just refactor this quickly..."
- "Let me update the file to reflect this..."
- Creating a TodoWrite list for implementation tasks
- Opening EnterPlanMode

**All of these violate this skill. Explain only. No changes.**

## Common Mistakes

| Mistake | Correct behavior |
|---|---|
| Editing a file "just to show the example" | Write the example inline in the chat |
| Suggesting follow-up changes unprompted | Only explain what was asked |
| Creating implementation todos | Explain only, no planning |
| Using `Bash` to run the app / tests | Not allowed under this skill |
