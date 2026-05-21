#!/usr/bin/env bash
set -euo pipefail

# Build mac-share
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
swiftc "$SCRIPT_DIR/mac-share.swift" -o "$SCRIPT_DIR/mac-share" -framework AppKit

# Target directory (default relative to this script). You can pass a custom target as first arg.
DEFAULT_TARGET_REL="../capturableExtension/src/jvmMain/resources/native/macos"
TARGET_DIR="${1:-$SCRIPT_DIR/$DEFAULT_TARGET_REL}"

# Ensure target exists and move (overwrite if exists)
mkdir -p "$TARGET_DIR"
mv -f "$SCRIPT_DIR/mac-share" "$TARGET_DIR/"

echo "Built mac-share -> $TARGET_DIR/mac-share"
