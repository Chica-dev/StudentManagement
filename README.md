# JavaCourse
Javaコースで作成する受講生管理システム用プロジェクト

---

## 環境構築・起動手順

### 前提条件
- Java 17 以上
- MySQL 8.0 以上
- Gradle

### 1. データベースの準備

MySQLで `StudentManagement` データベースを作成してください。

```sql
CREATE DATABASE StudentManagement;
```

### 2. 環境変数の設定

`.env.example` をコピーして `.env` を作成し、実際の値を設定してください。

```bash
cp .env.example .env
```

`.env` を編集：DB_PASSWORD=自分のMySQLパスワード

> ⚠️ `.env` は絶対にGitにコミットしないでください（`.gitignore` で除外済み）。

### 3. アプリの起動

```bash
./gradlew bootRun
```

---

## 必要な環境変数一覧

| 変数名 | 説明 |
|--------|------|
| `DB_PASSWORD` | MySQLのパスワード |