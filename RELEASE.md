# Release Process

This repository has automated version bumping and release creation.

## Quick Release (GitHub UI - Recommended)

1. Go to **Actions** tab on GitHub
2. Select **Release** workflow
3. Click **Run workflow**
4. Select:
   - **Project**: `piggy-admin`, `piggy-build`, `piggy-inventory`, or `piggy-lib`
   - **Version**: e.g., `1.2.3`
5. Click **Run workflow**

**The workflow will automatically:**
- ✅ Update `gradle.properties` with new version
- ✅ If releasing `piggy-lib`: Update all READMEs and fabric.mod.json dependencies
- ✅ Commit changes with message: `chore: bump {project} to v{version}`
- ✅ Create git tag: `{project}-v{version}`
- ✅ Build the project
- ✅ Create GitHub Release with `.jar` files

---

## Local Release (Alternative)

For offline scenarios or manual control:

```bash
# From repository root
./scripts/bump-version.sh piggy-build 1.3.0

# Review changes
git show HEAD

# Push to GitHub
git push origin main --tags
```

The script will:
- ✅ Update all necessary files
- ✅ Create commit and tag locally
- ⚠️ You must push manually

---

## Version Number Format

Use semantic versioning: `MAJOR.MINOR.PATCH`

- **MAJOR**: Breaking changes
- **MINOR**: New features (backward compatible)
- **PATCH**: Bug fixes

Examples: `1.0.0`, `1.2.3`, `2.0.0`

---

## What Gets Updated

### For `piggy-lib`
- `gradle.properties`: `lib_version`
- All project READMEs: piggy-lib dependency version
- All `fabric.mod.json` files: `"piggy-lib": ">=X.X.X"`

### For Other Projects
- `gradle.properties`: `{project}_version`
- That's it! `fabric.mod.json` uses `${version}` automatically

---

## Troubleshooting

**"No changes to commit" error:**
- Version is already set (check `gradle.properties`)
- Run with different version number

**Build fails:**
- Check that code compiles locally first: `./gradlew :{project}:build`
- Fix any compilation errors before releasing

**Tag already exists:**
- That version was already released
- Use a higher version number
- Or delete old tag: `git tag -d {project}-v{version}` (not recommended)
