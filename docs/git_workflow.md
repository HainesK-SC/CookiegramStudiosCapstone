# Workflow - Capstone Team

Purpose: in case you get lost in the git workflow, use this as a reference.

## Branch Overview

| Branch | Purpose                                                     |
|---|-------------------------------------------------------------|
| `development` | **Where all work happens** — feature development, bug fixes |
| `staging` | Pre-production testing — mirrors what's going to main      |
| `main` | Production-ready code — stable, deployable                  |

**Rule of thumb:** You always branch from and commit to `development`. Changes flow: `development → staging → main`.

Use IntelliJ's built-in Git integration to manage branches and commits.

All the commands listed below are a last resort and as reference if you're stuck.

---

## 🔁 Every Time You Log In (Start of Session Checklist)

Run these commands **every single time** before touching any code:

```bash
# 1. Fetch all remote changes (doesn't modify your files yet)
git fetch --all

# 2. Switch to development and pull latest
git checkout development
git pull origin development

# 3. Sync staging with latest remote
git checkout staging
git pull origin staging

# 4. Sync main with latest remote
git checkout main
git pull origin main

# 5. Go back to development — this is where you work
git checkout development
```

---

## ✍️ Daily Work — Making and Committing Changes

All your coding happens on `development`. Here's the full commit and push flow:

```bash
# Make sure you're on development
git checkout development

# Pull any new teammate changes before you start coding
git pull origin development

# ... do your work ...

# Check what files changed
git status

# Stage specific files (preferred — be intentional)
git add src/YourFile.java
git add src/AnotherFile.java

# OR stage everything changed (use carefully)
git add .

# Commit with a descriptive message
git commit -m "feat: add user login validation logic"

# Push to remote development branch
git push origin development
```

---

## 🔀 Merging Development → Staging → Main

Ensure you also write test files in development - and locally run them.

Once a feature, test cases pass, or set of commits on `development` is tested and ready to move forward:

### Step 1: Merge development into staging

```bash
git checkout staging
git pull origin staging           # get latest staging first
git merge development             # bring in development changes
git push origin staging           # push merged staging to remote
```

### Step 2: Merge staging into main (when ready for production)

```bash
git checkout main
git pull origin main              # get latest main first
git merge staging                 # bring in staging changes
git push origin main              # push merged main to remote
```

### Step 3: Go back to development

```bash
git checkout development
```

> ✅ After merging, all three branches should reflect the same codebase.

---

## 🤝 Keeping in Sync as a Team of 3

Before pushing, **always pull first** to avoid conflicts:

```bash
# Before any push, do this:
git pull origin development
# Resolve any conflicts if they appear, then:
git push origin development
```

If IntelliJ flags a conflict:
1. Open the conflicted file — IntelliJ has a built-in merge tool (look for the blue/red highlights)
2. Accept the correct changes (yours, theirs, or both)
3. Mark as resolved, then commit and push

---

## ⚠️ Rules for the Team

1. **Never commit directly to `main`** — always go through `development` → `staging` → `main`
2. **Always pull before you push**
3. **Communicate before merging to staging or main** — give a heads-up in your group chat
4. **Write meaningful commit messages** — your teammates will thank you
5. **Don't push broken code to development** — at minimum, make sure it compiles

---