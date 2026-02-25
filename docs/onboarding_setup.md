4
# Git Clone & Onboarding Setup

Purpose: this is for anyone who wants to contribute to the project -- DO THIS ONLY ONCE
This file is authored by Matthew Samaha

---

## 🚀 Getting Started - First Time Setup (Onboarding)

If you're new to this project, follow these steps to get your local environment set up:

### Preparation
Before you begin, make sure you have:
- [ ] Git installed (check with `git --version`)
- [ ] Java JDK 17+ installed (check with `java -version`)
- [ ] Maven installed (check with `mvn -v`) OR Gradle
- [ ] IntelliJ IDEA or Eclipse IDE
- [ ] GitHub account with access to the repository
- [ ] If working in Eclipse, ensure you have the [EGit - Git Integration for Eclipse](https://marketplace.eclipse.org/content/egit-git-integration-eclipse) installed

**You can do this inside Eclipse's Marketplace or by downloading the plugin directly from the link.**


### Step 1: Clone the Repository

**Command Line:**
```bash
# Navigate to where you want to store the project
cd ~/Documents/Projects  # or wherever you keep your code

# Clone the repository 
git clone https://github.com/HainesK-SC/CookiegramStudiosCapstone.git

# Navigate into the project directory
cd CookiegramStudiosCapstone
```

**IntelliJ Method:**
1. Open IntelliJ IDEA
2. Import **File** → **New** → **Project from Version Control**
3. Paste your repository URL: `https://github.com/HainesK-SC/CookiegramStudiosCapstone.git`
4. Choose a directory location
5. Click **Clone**

**Eclipse Method:**
1. Open Eclipse
2. Click **File** → **Import** → **Git** → **Projects from Git** → **Next**
3. Select **Clone URI** → **Next**
4. Paste your repository URL: `https://github.com/HainesK-SC/CookiegramStudiosCapstone.git`
5. Enter your GitHub credentials if prompted → **Next**
6. Select all branches (main, development, staging) → **Next**
7. Choose a directory location → **Next**
8. Select **Import existing Eclipse projects** or **Import as general project** → **Next**
9. Click **Finish**

### Step 2: Verify All Branches Are Available

**Command Line:**
```bash
# Fetch all branches from remote
git fetch --all

# List all branches (local and remote)
git branch -a

# You should see:
#   * main
#     development
#     staging
#     remotes/origin/main
#     remotes/origin/development
#     remotes/origin/staging
```

**IntelliJ Method:**
1. Look at the bottom-right corner (current branch name)
2. Click it to open the branch menu
3. You should see all branches under **Local Branches** and **Remote Branches**

**Eclipse Method:**
1. Right-click on project → **Team** → **Show in Repositories View**
2. Expand your repository → **Branches** → **Remote Tracking**
3. You should see `origin/main`, `origin/development`, `origin/staging`

### Step 3: Set Up Local Branches

**Command Line:**
```bash
# Create and checkout development branch (tracks remote)
git checkout development

# Create and checkout staging branch (tracks remote)
git checkout staging

# Go back to main
git checkout main

# Finally, switch to development — this is where you'll work
git checkout development
```

**IntelliJ Method:**
1. Click the branch name in bottom-right corner
2. Under **Remote Branches** → **origin**, right-click `development`
3. Select **Checkout** (this creates a local tracking branch)
4. Repeat for `staging`
5. Switch to `development` branch to start working

**Eclipse Method:**
1. Right-click project → **Team** → **Switch To** → **Other...**
2. Expand **Remote Tracking** → Select `origin/development`
3. Click **Checkout...** → Select **Checkout as New Local Branch** → **Finish**
4. Repeat for `origin/staging`
5. Switch back to `development`: Right-click project → **Team** → **Switch To** → **development**

### Step 4: Set Up Your Git Configuration (If Not Already Done)

**Command Line:**
```bash
# Set your name and email (used for commits)
git config --global user.name "Your Name"
git config --global user.email "your.email@example.com"

# Verify configuration
git config --global --list
```

**IntelliJ Method:**
1. Go to **File** → **Settings** (or **IntelliJ IDEA** → **Preferences** on Mac)
2. Navigate to **Version Control** → **Git**
3. Your name and email should already be configured from global Git settings
4. If not, set them via command line above

**Eclipse Method:**
1. Go to **Window** → **Preferences** (or **Eclipse** → **Preferences** on Mac)
2. Navigate to **Team** → **Git** → **Configuration**
3. Click **Add Entry...**
    - Key: `user.name`, Value: `Your Name` → **OK**
    - Key: `user.email`, Value: `your.email@example.com` → **OK**
4. Click **Apply and Close**

### Step 5: Install Project Dependencies

**Command Line:**
```bash
# For a Spring Boot / Maven project:
mvn clean install

# OR if using Gradle:
./gradlew build

# Run the application to verify everything works
mvn spring-boot:run
# OR
./gradlew bootRun
```

**IntelliJ Method:**
1. IntelliJ should auto-detect Maven/Gradle and prompt you to import
2. Click **Load Maven Project** or **Import Gradle Project** in the popup
3. Wait for dependencies to download (check progress in bottom-right)
4. Click the green **Run** button (or **Shift+F10**) to test the application

**Eclipse Method:**
1. Right-click project → **Maven** → **Update Project** (or **Alt+F5**)
2. Check **Force Update of Snapshots/Releases** → **OK**
3. Wait for dependencies to download (check progress in bottom-right)
4. Right-click project → **Run As** → **Spring Boot App** (or **Maven build** with goal `spring-boot:run`)

### Step 6: Verify You're Ready to Work

**Command Line:**
```bash
# Check you're on development branch
git branch
# Should show: * development

# Check status
git status
# Should show: "On branch development, Your branch is up to date with 'origin/development'"

# You're ready! 🎉
```

**IntelliJ Method:**
- Check bottom-right corner shows `development` branch
- Run the application successfully
- You're ready! 🎉

**Eclipse Method:**
- Check bottom-right corner or status bar shows `development` branch
- Run the application successfully
- You're ready! 🎉

---

## 📚 Understanding the Branches

Before you start working, understand our branch structure:

| Branch | Purpose | Can You Push Directly? |
|---|---|---|
| `development` | Main development branch — base for all feature branches | ✅ Yes (via PR from issue branches) |
| `staging` | Pre-production testing — mirrors what's going to main | ✅ Yes (merge from development) |
| `main` | Production-ready code — stable, deployable | ❌ No (PR only from staging) |
| `feature/*` or `fix/*` | Issue-specific branches — created from GitHub issues | ✅ Yes (your work happens here) |

**Workflow Direction:**
```
issue-branch → development → staging → main (via PR)
```

---

## 🎫 Issue-Driven Development Workflow

### Step 1: Create a GitHub Issue
1. Go to your repository on GitHub
2. Click **Issues** → **New Issue**
3. Write a clear title and description
4. Assign yourself to the issue
5. Add relevant labels (`bug`, `enhancement`, `documentation`, etc.)
6. Note the issue number (e.g., `#42`)

**Example Issue:**
```
Title: Fix @Enumerated annotation on String field in Promotion entity

Description:
## Bug Description
Application fails to start due to incorrect type on promoType field...

## Solution
Change field type from String to PromotionTypes enum...

Assignee: @your-username
Labels: bug, priority: high
```

**Another Example**
If you're working on a user story from Trello, just use the title and description from the card.


### Step 2: Create a Branch from the Issue
1. On the issue page, look for **"Create a branch"** link (right sidebar under "Development")
2. Click it and configure:
    - **Branch name:** Auto-generated (e.g., `42-fix-enumerated-annotation`) or customize it
    - **Branch source:** Select `development` (not `main`!)
    - **Repository destination:** Your repository
3. Click **"Create branch"**
4. GitHub will create the branch and link it to the issue

### Step 3: Fetch and Checkout the New Branch Locally

**Command Line:**
```bash
# 1. Fetch the new branch from remote
git fetch origin

# 2. Checkout the issue branch
git checkout 42-fix-enumerated-annotation

# OR if Git doesn't find it automatically:
git checkout -b 42-fix-enumerated-annotation origin/42-fix-enumerated-annotation
```

**IntelliJ Method:**
1. Click **Git** → **Fetch** (or **Ctrl+T** → **Fetch**)
2. In the bottom-right corner, click the branch name
3. Find your issue branch under **Remote Branches** → **origin/42-fix-enumerated-annotation**
4. Right-click → **Checkout**

**Eclipse Method:**
1. Right-click project → **Team** → **Fetch from Upstream**
2. Right-click project → **Team** → **Switch To** → **Other...**
3. Expand **Remote Tracking** → Find `origin/42-fix-enumerated-annotation`
4. Click **Checkout...** → Select **Checkout as New Local Branch** → **Finish**

---

## 🔁 Every Time You Log In (Start of Session Checklist)

**Command Line:**
```bash
# 1. Fetch all remote changes
git fetch --all

# 2. Switch to development and pull latest
git checkout development
git pull origin development

# 3. If you're working on an issue branch, update it with latest development
git checkout 42-fix-enumerated-annotation
git merge development  # OR: git rebase development (if you prefer)
```

**IntelliJ Method:**
1. Click **Git** → **Fetch** (or **Ctrl+T**)
2. Switch to `development`: Click branch name in bottom-right → select `development`
3. Click **Git** → **Pull** (or **Ctrl+T** → **Pull**)
4. Switch to your issue branch: Click branch name → select `42-fix-enumerated-annotation`
5. Click **Git** → **Merge** → Select `development` → **Merge**

**Eclipse Method:**
1. Right-click project → **Team** → **Fetch from Upstream**
2. Right-click project → **Team** → **Switch To** → **development**
3. Right-click project → **Team** → **Pull**
4. Right-click project → **Team** → **Switch To** → **42-fix-enumerated-annotation**
5. Right-click project → **Team** → **Merge** → Select `development` → **Merge**

---

## ✍️ Daily Work — Making and Committing Changes on Issue Branch

All your coding happens on your **issue-specific branch**. Here's the full workflow:

**Command Line:**
```bash
# Make sure you're on your issue branch
git checkout 42-fix-enumerated-annotation

# Pull any new changes (teammates might have updated development)
git pull origin development  # keep your branch up-to-date

# ... do your work ...

# Check what files changed
git status

# Stage specific files (preferred — be intentional)
git add src/YourFile.java
git add src/AnotherFile.java

# OR stage everything changed (use carefully)
git add .

# Commit with a descriptive message that references the issue
git commit -m "fix: change promoType to PromotionTypes enum (#42)"

# Push to remote issue branch
git push origin 42-fix-enumerated-annotation
```

**IntelliJ Method:**
1. Verify you're on `42-fix-enumerated-annotation` (bottom-right corner)
2. Make your code changes
3. Click **Git** → **Commit** (or **Ctrl+K**)
4. Select files to commit (checkboxes on left)
5. Write commit message: `fix: change promoType to PromotionTypes enum (#42)`
6. Click **Commit and Push** (or just **Commit** then **Git** → **Push** later)
7. Verify branch name in push dialog, then click **Push**

**Eclipse Method:**
1. Verify you're on `42-fix-enumerated-annotation` (bottom-right status bar)
2. Make your code changes
3. Right-click project → **Team** → **Commit**
4. Select files to commit (checkboxes in "Unstaged Changes")
5. Click **Add Selected Files to Index** (or drag to "Staged Changes")
6. Write commit message: `fix: change promoType to PromotionTypes enum (#42)`
7. Click **Commit and Push** (or just **Commit** then **Team** → **Push to Upstream** later)
8. Verify branch name in push dialog, then click **Push**

**Commit Message Format:**
- `fix: description (#issue-number)` — for bug fixes
- `feat: description (#issue-number)` — for new features
- `docs: description (#issue-number)` — for documentation
- `refactor: description (#issue-number)` — for code refactoring
- `test: description (#issue-number)` — for test additions

---

## 🔀 Merging Your Issue Branch Back to Development

Once your work is complete and tested:

### Option A: Create Pull Request (Recommended)

1. **On GitHub:**
    - Go to **Pull Requests** → **New Pull Request**
    - Set base: `development`, compare: `42-fix-enumerated-annotation`
    - Title: Use your commit message or issue title
    - Description: Add details, screenshots, testing notes
    - Link the issue (GitHub may auto-link it)
    - Request review from teammates
    - Add labels

2. **Get Review & Approval**

3. **Merge the PR** (options):
    - **Squash and merge** (recommended for clean history)
    - **Merge commit** (preserves all commits)
    - **Rebase and merge** (linear history)

4. **Delete the branch** after merge (GitHub will prompt you)

5. **Pull updated development locally:**

**Command Line:**
```bash
git checkout development
git pull origin development
```

**IntelliJ Method:**
- Switch to `development` branch (bottom-right)
- Click **Git** → **Pull**

**Eclipse Method:**
- Right-click project → **Team** → **Switch To** → **development**
- Right-click project → **Team** → **Pull**

### Option B: Direct Merge (Use sparingly, when PR not needed)

**Command Line:**
```bash
# Switch to development
git checkout development

# Pull latest development
git pull origin development

# Merge your issue branch
git merge 42-fix-enumerated-annotation

# Push to development
git push origin development

# Delete the issue branch locally
git branch -d 42-fix-enumerated-annotation

# Delete the issue branch remotely
git push origin --delete 42-fix-enumerated-annotation
```

**IntelliJ Method:**
1. Switch to `development` branch
2. Click **Git** → **Pull**
3. Click **Git** → **Merge** → Select `42-fix-enumerated-annotation` → **Merge**
4. Click **Git** → **Push**
5. Right-click branch `42-fix-enumerated-annotation` → **Delete**

**Eclipse Method:**
1. Right-click project → **Team** → **Switch To** → **development**
2. Right-click project → **Team** → **Pull**
3. Right-click project → **Team** → **Merge** → Select `42-fix-enumerated-annotation` → **Merge**
4. Right-click project → **Team** → **Push to Upstream**
5. Right-click project → **Team** → **Advanced** → **Delete Branch** → Select `42-fix-enumerated-annotation`

---

## 🔀 Promoting Changes: Development → Staging → Main

Once features are tested and ready in `development`:

### Step 1: Merge development into staging

**Command Line:**
```bash
git checkout staging
git pull origin staging           # get latest staging first
git merge development             # bring in development changes
git push origin staging           # push merged staging to remote
```

**IntelliJ Method:**
1. Switch to `staging` branch (bottom-right)
2. Click **Git** → **Pull**
3. Click **Git** → **Merge** → Select `development` → **Merge**
4. Click **Git** → **Push**

**Eclipse Method:**
1. Right-click project → **Team** → **Switch To** → **staging**
2. Right-click project → **Team** → **Pull**
3. Right-click project → **Team** → **Merge** → Select `development` → **Merge**
4. Right-click project → **Team** → **Push to Upstream**

### Step 2: Create Pull Request for staging → main

**⚠️ IMPORTANT: `main` is protected — you CANNOT push directly!**

1. **On GitHub:**
    - Go to **Pull Requests** → **New Pull Request**
    - Set base: `main`, compare: `staging`
    - Title: "Release v1.2.0" or "Merge staging into main"
    - Description: List all issues/features included
        - Use "Closes #42, Closes #43, Resolves #44" to auto-close issues
    - Request reviews from all teammates
    - Add `release` or `production` label

2. **Team Review** — everyone should approve

3. **Merge the PR** once approved

4. **Pull the updated main locally:**

**Command Line:**
```bash
git checkout main
git pull origin main

# Update development and staging to match
git checkout development
git merge main
git push origin development

git checkout staging  
git merge main
git push origin staging
```

**IntelliJ Method:**
1. Switch to `main` branch → Pull
2. Switch to `development` branch → Merge `main` → Push
3. Switch to `staging` branch → Merge `main` → Push

**Eclipse Method:**
1. Switch to `main` → Pull
2. Switch to `development` → Merge `main` → Push to Upstream
3. Switch to `staging` → Merge `main` → Push to Upstream

---

## 🤝 Keeping in Sync as a Team of 3

### Before Starting Work

**Command Line:**
```bash
# Always start from a fresh development branch
git checkout development
git pull origin development

# Then fetch and checkout your issue branch
git fetch origin
git checkout 42-your-issue-branch
git merge development  # bring in latest changes
```

**IntelliJ/Eclipse:**
- Switch to `development` → Pull
- Fetch all changes
- Switch to your issue branch
- Merge `development` into your issue branch

### Handling Conflicts

**IntelliJ:**
1. When a conflict occurs, IntelliJ shows a **Merge Conflicts** dialog
2. Click **Merge** to open the 3-way merge tool
3. Left pane = your changes, right pane = incoming changes, center = result
4. Click **Accept Left** / **Accept Right** / **Accept Both** for each conflict
5. Or manually edit the center pane
6. Click **Apply** when done
7. Commit the merge

**Eclipse:**
1. When a conflict occurs, files are marked with a conflict icon
2. Right-click conflicted file → **Team** → **Merge Tool**
3. Eclipse opens a comparison view
4. Edit the file to resolve conflicts (look for `<<<<<<<`, `=======`, `>>>>>>>` markers)
5. Save the file
6. Right-click file → **Team** → **Add to Index**
7. Commit the merge

**Command Line (if IDE fails):**
1. Open conflicted file in text editor
2. Look for conflict markers:
```
<<<<<<< HEAD
your changes
=======
their changes
>>>>>>> branch-name
```
3. Edit to keep the correct version (or combine both)
4. Remove the conflict markers
5. `git add <file>`
6. `git commit -m "Merge development into 42-fix-enumerated-annotation"`

---

## 📋 Closing Issues

Issues are automatically closed when:
- You use `Closes #42`, `Fixes #42`, or `Resolves #42` in a PR description
- The PR is merged into `main`

You can also manually close issues on GitHub after merging to `development`.

---

## ⚠️ Rules for the Team

1. **Always create an issue before starting work** — even for small fixes
2. **Always create a branch from the issue** — use `development` as the branch source
3. **Never commit directly to `main`** — it's protected! Always use a PR from `staging`
4. **Always create a PR from your issue branch to `development`** — for code review
5. **Reference issue numbers in commits** — enables automatic tracking
6. **Always pull before you push**
7. **Communicate before merging to staging or creating a PR to main**
8. **Write meaningful commit messages**
9. **Don't push broken code** — at minimum, make sure it compiles
10. **Review PRs promptly** — don't leave teammates blocked
11. **Delete branches after merging** — keeps the repository clean

---

## 🚀 Quick Reference

**First time setup (Command Line):**
```bash
# Clone the repo
git clone https://github.com/HainesK-SC/CookiegramStudiosCapstone.git
cd CookiegramStudiosCapstone

# Fetch and checkout all branches
git fetch --all
git checkout development
git checkout staging
git checkout main
git checkout development  # start here

# Install dependencies and run
mvn clean install
mvn spring-boot:run
```

**Full workflow for a new issue (Command Line):**
```bash
# 1. Create issue on GitHub (#42)
# 2. Click "Create a branch" → Source: development
# 3. Fetch and checkout locally
git fetch origin
git checkout 42-fix-enumerated-annotation

# 4. Do your work, commit, and push
git add .
git commit -m "fix: change promoType field type (#42)"
git push origin 42-fix-enumerated-annotation

# 5. Create PR on GitHub: 42-fix-enumerated-annotation → development
# 6. Get review, merge PR, delete branch
# 7. Update local development
git checkout development
git pull origin development
```

**Ready for production (staging → main):**
```bash
# 1. Merge dev to staging
git checkout staging
git merge development
git push origin staging

# 2. Create PR on GitHub: staging → main (with "Closes #42, #43")
# 3. Get team approval and merge PR
# 4. Sync everything
git checkout main
git pull origin main
git checkout development
git merge main
git push origin development
```

---

## 📊 Branch Naming Convention

GitHub auto-generates branch names from issues:
- `42-fix-enumerated-annotation` (from issue #42)
- `43-add-user-authentication` (from issue #43)

You can customize the name when creating the branch, but keep it:
- Lowercase
- Hyphen-separated
- Descriptive
- Prefixed with issue number

---

## 🆘 Common Issues & Solutions

**Problem:** "fatal: remote origin already exists"
- **Solution:** You've already cloned. Just `cd` into the project directory.

**Problem:** Can't see remote branches in IntelliJ/Eclipse
- **Solution:**
    - **IntelliJ:** Git → Fetch, then check bottom-right branch menu
    - **Eclipse:** Right-click project → Team → Fetch from Upstream

**Problem:** Branch is behind origin/development
- **Solution:** Pull or merge origin/development into your branch

**Problem:** Merge conflict
- **Solution:**
    - **IntelliJ:** Use the 3-way merge tool (accept left/right/both)
    - **Eclipse:** Use Team → Merge Tool or manually edit conflicts
    - **Command Line:** Edit files, remove markers, `git add`, `git commit`

**Problem:** Accidentally committed to wrong branch
- **Solution:** Ask for help in team chat before pushing!

**Problem:** Eclipse shows "Project configuration is not up-to-date"
- **Solution:** Right-click project → Maven → Update Project → Force Update

**Problem:** IntelliJ not recognizing as Maven/Gradle project
- **Solution:** Right-click `pom.xml` or `build.gradle` → Add as Maven/Gradle Project

---

## 🔧 IDE-Specific Tips

### IntelliJ IDEA
- **Keyboard Shortcuts:**
    - `Ctrl+K` / `Cmd+K` — Commit
    - `Ctrl+Shift+K` / `Cmd+Shift+K` — Push
    - `Ctrl+T` / `Cmd+T` — Update Project (Pull)
- **Git Tool Window:** View → Tool Windows → Git (shows branches, commits, log)
- **Interactive Rebase:** Right-click commit → Interactive Rebase to clean up history

### Eclipse
- **Keyboard Shortcuts:**
    - `Ctrl+#` / `Cmd+#` — Quick Commit
    - `Alt+Shift+P` — Push to Upstream
- **Git Staging View:** Window → Show View → Other → Git → Git Staging
- **History View:** Right-click project → Team → Show in History

---

This workflow ensures:
- ✅ New team members can quickly get started with their preferred IDE
- ✅ All work is tracked via GitHub issues and linked branches
- ✅ Code review happens via PRs before merging to `development`
- ✅ `main` branch protection is respected (PR-only)
- ✅ Clean branch management with auto-linking
- ✅ Better team collaboration and transparency
- ✅ IDE flexibility — use IntelliJ, Eclipse, or command line