# Git Workflow Guide - Capstone Team

**Author:** Matthew Samaha

## Branch Structure

| Branch | Purpose | Direct Push? |
|--------|---------|-------------|
| `development` | Main development | ✅ Yes |
| `staging` | Pre-production | ✅ Yes |
| `main` | Production | ❌ No (PR only) |
| `issue/*` | Feature/fix work | ✅ Yes |

**Flow:** `issue-branch → development → staging → main`

---

## Two Workflow Options

### Option A: Issue-Based (Recommended for features/bugs)

```bash
# 1. Create GitHub issue (#42) → "Create branch" from development
# 2. Fetch and start
git fetch origin
git checkout 42-fix-something
git merge development

# 3. Work and push
git add [files]
git commit -m "fix: description (#42)"
git push origin 42-fix-something

# 4. Create PR on GitHub → merge → delete branch
git checkout development && git pull
```

### Option B: Direct (Quick fixes only)

```bash
git checkout development
git pull origin development
# make changes...
git add [files]
git commit -m "fix: description"
git push origin development
```

---

## Daily Start Routine

```bash
git fetch --all
git checkout development
git pull origin development
# If on issue branch: git checkout [branch] && git merge development
```

---

## Promoting to Production

```bash
# Development → Staging
git checkout staging && git pull && git merge development && git push

# Staging → Main (PR required)
# 1. Create PR on GitHub: staging → main
# 2. Team review → merge
# 3. Sync all branches:
git checkout main && git pull
git checkout development && git merge main && git push
git checkout staging && git merge main && git push
```

---

## Commit Format

- `fix: description (#42)` - bug fixes
- `feat: description (#42)` - features
- `docs: description (#42)` - documentation
- `refactor: description (#42)` - refactoring

---

## Team Rules

1. Pull before push
2. Never push to `main` directly
3. Communicate before staging/main merges
4. Test before pushing
5. Review PRs promptly
6. Delete merged branches

---

## Quick Fixes

**Merge conflict:**
```bash
# Edit file, remove <<<<<<< ======= >>>>>>> markers
git add [file]
git commit -m "Merge development"
```

**Branch behind?** `git merge development`

**Can't see branches?** `git fetch --all`

---

## Complete Examples

**Issue workflow:**
```bash
git fetch origin
git checkout 42-fix-bug
git merge development
# work...
git add . && git commit -m "fix: description (#42)"
git push origin 42-fix-bug
# PR on GitHub → merge
git checkout development && git pull
```

**Direct workflow:**
```bash
git checkout development && git pull
# work...
git add . && git commit -m "fix: quick change"
git push origin development
```