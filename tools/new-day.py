import shutil
import subprocess
import sys
import os
import urllib.error
import urllib.request
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

print(f"Create directory {inputDir}")
os.makedirs(inputDir, exist_ok=True)

if path.exists(inputFile):
    print(f"File {inputFile} already exists, skipping download")
else:
    print(f"Download from {downloadPath} to {inputFile}")
    opener = urllib.request.build_opener()
    opener.addheaders.append(("Cookie", f"session={sessionId}"))
    opener.addheaders.append(("User-Agent", "https://github.com/ivan-gusiev/"))
    try:
        response = opener.open(downloadPath)
        content = bytes(response.read())

        with open(inputFile, "w") as file:
            file.write(content.decode("utf-8", "ignore"))
    except urllib.error.HTTPError as e:
        print(e.reason)

print(f"Create file {ktFile}, copied from {ktTemplate}")
shutil.copy(ktTemplate, ktFile)

print(f"Template expansion on {ktFile}...")
# Read in the file
with open(ktFile, "r") as file:
    body = file.read()

# Replace the target string
body = body.replace("package template\n", "")
body = body.replace('@Suppress("unused")\n', "")
body = body.replace("Day0", f"Day{day}")
body = body.replace("TemplateDay", str(day))
body = body.replace("TemplateYear", str(year))

# Write the file out again
with open(ktFile, "w") as file:
    file.write(body)

print("DONE!")