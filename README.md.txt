# Storage Hygiene Advisor 🧹💾

A smart desktop application built using Java that analyzes storage usage, detects duplicate files, and provides insights to improve system storage hygiene.

---

## 📌 Overview

Storage Hygiene Advisor helps users clean and optimize their system storage by:

- Scanning folders recursively
- Detecting duplicate files
- Categorizing files (Images, Videos, Docs, Others)
- Visualizing storage usage
- Generating reports

---

## 🚀 Features

- 📂 Folder selection using GUI
- 🔍 Recursive file scanning
- ♻️ Duplicate file detection
- 📊 Storage usage visualization (graph)
- 📁 File categorization:
  - Images
  - Videos
  - Documents
  - Others
- 📈 Real-time progress bar
- 📄 Export report as `.txt`

---

## 🧠 Logic Used

- Duplicate detection based on:
  - File name + file size
- File classification using extensions:
  - `.jpg`, `.png` → Images
  - `.mp4`, `.mkv` → Videos
  - `.pdf`, `.doc` → Documents
- Storage analysis using file size aggregation

---

## 🖥️ Tech Stack

- Java (Core + Swing GUI)
- AWT (Graphics for visualization)
- File Handling (Java IO)

---

## 📸 Output Screenshots



### 📂 Duplicate Files
![Duplicates](screenshots/output2.png)

### 📋 Report Output
![Report](screenshots/output1.png)

---

## 📂 Project Structure


StorageHygieneAdvisor/
│── StorageHygieneAdvisor.java # Main application
│── screenshots/ # Output images
│── report.txt # Generated report (after export)


---

## ▶️ How to Run

### Step 1: Compile
```bash
javac StorageHygieneAdvisor.java
Step 2: Run
java StorageHygieneAdvisor