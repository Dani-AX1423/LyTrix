#!/bin/bash
# compile_and_run.sh

FILENAME=$1
BIN_DIR="$HOME/Projects/MinLang/bin/SampleClassFiles"

# Ensure bin directory exists
mkdir -p "$BIN_DIR"

# Search for the source file in subfolders (excluding bin)
SRC_FILE=$(find . -path "./bin" -prune -o -type f -name "$FILENAME" -print | head -n 1)

if [[ -z "$SRC_FILE" ]]; then
    echo "File '$FILENAME' not found in project subfolders!"
    exit 1
fi

# Determine extension
EXT="${SRC_FILE##*.}"

if [[ $EXT == "java" ]]; then
    echo "Compiling $SRC_FILE ..."
    sleep 0.100
    javac -d "$BIN_DIR" "$SRC_FILE"
    if [[ $? -ne 0 ]]; then
        echo "Compilation failed!"
        exit 1
    fi
    echo "Compilation Success!"
    CLASS_NAME=$(basename "$SRC_FILE" .java)
elif [[ $EXT == "class" ]]; then
    CLASS_NAME=$(basename "$SRC_FILE" .class)
else
    echo "File must be .java or .class!"
    exit 1
fi

# Search for the .class file inside bin folder
CLASS_FILE=$(find "$BIN_DIR" -type f -name "$CLASS_NAME.class" | head -n 1)

if [[ -z "$CLASS_FILE" ]]; then
    echo "Compiled .class file not found in $BIN_DIR!"
    exit 1
fi

# Run the class file
CLASS_DIR=$(dirname "$CLASS_FILE")
cd "$CLASS_DIR" || exit
echo "Running $CLASS_NAME ..."
echo ""
java "$CLASS_NAME"
