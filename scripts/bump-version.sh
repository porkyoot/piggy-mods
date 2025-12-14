#!/bin/bash
# Bump version script for local use
# Usage: ./scripts/bump-version.sh <project> <version>
# Example: ./scripts/bump-version.sh piggy-build 1.3.0

set -e

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <project> <version>"
    echo "Projects: piggy-lib, piggy-admin, piggy-build, piggy-inventory"
    echo "Example: $0 piggy-build 1.3.0"
    exit 1
fi

PROJECT=$1
VERSION=$2

# Validate project
if [[ ! "$PROJECT" =~ ^(piggy-lib|piggy-admin|piggy-build|piggy-inventory)$ ]]; then
    echo "Error: Invalid project '$PROJECT'"
    echo "Valid projects: piggy-lib, piggy-admin, piggy-build, piggy-inventory"
    exit 1
fi

# Validate version format
if [[ ! "$VERSION" =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "Error: Invalid version format '$VERSION'"
    echo "Expected format: X.Y.Z (e.g., 1.2.3)"
    exit 1
fi

echo "Bumping $PROJECT to version $VERSION..."

# Update gradle.properties
if [ "$PROJECT" = "piggy-lib" ]; then
    sed -i "s/^lib_version=.*/lib_version=$VERSION/" gradle.properties
    
    echo "Updating all READMEs with new piggy-lib dependency..."
    # Update all READMEs with new piggy-lib dependency version
    sed -i "s|\*\*\[Piggy Lib\][^:]*: >=.*|\*\*[Piggy Lib](https://github.com/porkyoot/piggy-lib)\*\*: >=$VERSION|" piggy-admin/README.md
    sed -i "s|\*\*\[Piggy Lib\][^:]*: >=.*|\*\*[Piggy Lib](https://github.com/porkyoot/piggy-lib)\*\*: >=$VERSION|" piggy-build/README.md
    sed -i "s|\*\*\[Piggy Lib\][^:]*: >=.*|\*\*[Piggy Lib](https://github.com/porkyoot/piggy-lib)\*\*: >=$VERSION|" piggy-inventory/README.md
    
    echo "Updating fabric.mod.json dependencies..."
    # Update fabric.mod.json dependencies
    sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-admin/src/main/resources/fabric.mod.json
    sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-build/src/main/resources/fabric.mod.json
    sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-inventory/src/main/resources/fabric.mod.json
    
    # Also update gametest fabric.mod.json if they exist
    [ -f "piggy-admin/src/gametest/resources/fabric.mod.json" ] && \
        sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-admin/src/gametest/resources/fabric.mod.json
    [ -f "piggy-build/src/gametest/resources/fabric.mod.json" ] && \
        sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-build/src/gametest/resources/fabric.mod.json
    [ -f "piggy-inventory/src/gametest/resources/fabric.mod.json" ] && \
        sed -i "s/\"piggy-lib\": \">=.*\"/\"piggy-lib\": \">=$VERSION\"/" piggy-inventory/src/gametest/resources/fabric.mod.json
    
elif [ "$PROJECT" = "piggy-admin" ]; then
    sed -i "s/^admin_version=.*/admin_version=$VERSION/" gradle.properties
elif [ "$PROJECT" = "piggy-build" ]; then
    sed -i "s/^build_version=.*/build_version=$VERSION/" gradle.properties
elif [ "$PROJECT" = "piggy-inventory" ]; then
    sed -i "s/^inventory_version=.*/inventory_version=$VERSION/" gradle.properties
fi

echo "✓ Updated gradle.properties"

# Create git commit
echo ""
echo "Committing changes..."
git add .
git commit -m "chore: bump $PROJECT to v$VERSION"

# If piggy-lib, also commit changes in submodules
if [ "$PROJECT" = "piggy-lib" ]; then
    echo ""
    echo "Committing dependency updates in submodules..."
    
    for submodule in piggy-admin piggy-build piggy-inventory; do
        cd "$submodule"
        if git diff --quiet; then
            echo "  No changes in $submodule"
        else
            git add README.md src/main/resources/fabric.mod.json src/gametest/resources/fabric.mod.json 2>/dev/null || true
            git commit -m "chore: update piggy-lib dependency to v$VERSION" || echo "  No changes to commit in $submodule"
            echo "  ✓ Committed changes in $submodule"
        fi
        cd ..
    done
    
    # Update parent repo to reference new submodule commits
    git add piggy-admin piggy-build piggy-inventory
    git commit --amend -m "chore: bump $PROJECT to v$VERSION"
fi

# Create git tag
TAG="${PROJECT}-v${VERSION}"
echo "Creating tag $TAG..."
git tag "$TAG"

echo ""
echo "✓ Version bump complete!"
echo ""
echo "Next steps:"
echo "  1. Review changes: git show HEAD"
echo "  2. Push changes: git push origin main"
echo "  3. Push tag: git push origin $TAG"
echo ""
echo "Or push everything at once:"
echo "  git push origin main --tags"
