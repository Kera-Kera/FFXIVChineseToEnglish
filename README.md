# FFXIVChnTextPatch-EN

English text patch tool for the FFXIV CN client (Shanda/盛趣). Replaces in-game text with English by injecting CSV files extracted from the Global EN client into the CN client's SqPack files.

Forked from [GpointChen/FFXIVChnTextPatch-GP](https://github.com/GpointChen/FFXIVChnTextPatch-GP).

---

## Usage

### Patching

1. Run `FFXIVChnTextPatchGP.exe`
2. Click **Set** to open settings
3. Set **Game Path** to your CN client root folder (the folder containing `game/ffxiv_dx11.exe`)
4. Set **File Lang** to `CSV`
5. Click **Confirm**
6. Click **Patch** and wait for it to complete

### Restoring

Click **Restore** to revert the game files back to the originals from the `backup/` folder.

> **Note:** Do not patch twice without restoring first. The second patch would back up already-patched files, overriding your previous back-ups.

---

## Updating after a game patch

When the game updates you will need to restore the game, then repatch. (You can either restore before the update, or after, doesn't seem to matter, ngl idk why xd).

After multiple game updates, the included CSVs may become outdated. To regenerate them you need a copy of the **Global EN client** (Steam or Square Enix store version) and Python 3.

Edit the paths at the top of `gen_english_csvs.py` to point to your Global EN SqPack folder (typically `FINAL FANTASY XIV Online\game\sqpack\ffxiv`) and the `resource/rawexd/` output folder next to the exe, then run:

```
python gen_english_csvs.py
```

---

## Disclaimer

This tool modifies client files. Use at your own risk. 
