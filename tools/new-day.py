import shutil
import subprocess
import sys
import os
from os import path


day = int(sys.argv[1])
year = "2023"
sessionId = "53616c7465645f5f06b0079ca6159f2162cf51aa667d9eb3cca3f4a0a57b5b6badaf81ab32bd11d9a340c4055c7c9b40a1de50a84565da6daac1b9a7541a4d62"



print(f"Initializing day {day}...")

ktTemplate = "./src/main/kotlin/template/Day0.kt"
ktFile = f"./src/main/kotlin/Day{day}.kt"
inputDir = path.expanduser(f"~/aoc/{year}/{day}")
inputFile = f"{inputDir}/input.txt"
downloadPath = f"https://adventofcode.com/{year}/day/{day}/input"

gitStatus = subprocess.check_output(["git", "status", "--porcelain"]).decode("utf-8", "ignore")
print(gitStatus)
if len(gitStatus.strip()) > 0:
    print(f"Git status not clean, please commit before the new day comes.")
    exit(1)

if path.exists(ktFile):
    print(f"[{ktFile}] already exists, aborting.")
    exit(1)

print(f"Create file {ktFile}, copied from {ktTemplate}")
shutil.copy(ktTemplate, ktFile)
print(f"Create directory {inputDir}")
os.makedirs(inputDir, exist_ok=True)
print(f"Download from {downloadPath} to {inputFile}")


print("DONE!")