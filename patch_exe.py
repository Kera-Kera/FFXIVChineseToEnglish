"""
Patches FFXIVChnTextPatchGP.exe with English UI class files.
Strategy:
  1. Find the PK (ZIP) start offset in the exe
  2. Extract the JAR portion to a temp file
  3. Update the JAR with new English class files using 'jar u'
  4. Splice back: exe_stub + updated_jar = new exe
"""
import os
import subprocess
import sys
import zipfile

EXE_PATH   = r"H:\China\FF14\ChnTextPatch\FFXIVChnTextPatchGP.exe"
OUT_PATH   = r"H:\China\FF14\ChnTextPatch\FFXIVChnTextPatchGP-EN.exe"
JAR_TMP    = r"H:\China\FF14\ChnTextPatch\tmp_embedded.jar"
CLASS_DIR  = r"H:\China\FF14\ChnTextPatch\FFXIVChnTextPatch-GP-master\build\recompile"
JAR_CMD    = r"C:\Program Files\Java\jdk-21\bin\jar.exe"

# ── 1. Find PK offset ────────────────────────────────────────────────────────
exe_data = open(EXE_PATH, 'rb').read()
pk_offset = exe_data.find(b'PK\x03\x04')
if pk_offset == -1:
    sys.exit("ERROR: PK signature not found in exe")
print(f"PK offset: {pk_offset}")

# ── 2. Extract JAR portion ───────────────────────────────────────────────────
jar_data = exe_data[pk_offset:]
with open(JAR_TMP, 'wb') as f:
    f.write(jar_data)
print(f"Extracted JAR: {len(jar_data)} bytes → {JAR_TMP}")

# Verify it opens as a ZIP
with zipfile.ZipFile(JAR_TMP, 'r') as z:
    names = z.namelist()
    print(f"JAR has {len(names)} entries")
    changed = [n for n in names if any(
        n.endswith(p) for p in [
            'Language.class',
            'TextPatchPanel.class', 'TextPatchPanel$1.class', 'TextPatchPanel$2.class',
            'ConfigApplicationPanel.class', 'ConfigApplicationPanel$1.class', 'ConfigApplicationPanel$2.class',
            'ReplaceThread.class', 'RollbackThread.class',
        ]
    )]
    print(f"Entries to replace: {changed}")

# ── 3. Update JAR with English class files ───────────────────────────────────
# jar uf tmp.jar -C class_dir .
#   → updates entries matching files under class_dir
result = subprocess.run(
    [JAR_CMD, 'uf', JAR_TMP, '-C', CLASS_DIR, '.'],
    capture_output=True, text=True
)
if result.returncode != 0:
    print("jar stdout:", result.stdout)
    print("jar stderr:", result.stderr)
    sys.exit(f"ERROR: jar update failed (exit {result.returncode})")
print("JAR updated successfully.")

# Verify the replacement worked
with zipfile.ZipFile(JAR_TMP, 'r') as z:
    for entry in changed:
        info = z.getinfo(entry)
        print(f"  {entry}: {info.file_size} bytes")

# ── 4. Splice back: exe stub + updated JAR ───────────────────────────────────
exe_stub = exe_data[:pk_offset]
updated_jar = open(JAR_TMP, 'rb').read()
with open(OUT_PATH, 'wb') as f:
    f.write(exe_stub)
    f.write(updated_jar)
print(f"Written: {OUT_PATH} ({len(exe_stub) + len(updated_jar)} bytes)")

# Cleanup temp JAR
os.remove(JAR_TMP)
print("Done.")
