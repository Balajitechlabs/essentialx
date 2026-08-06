#!/usr/bin/env python3
import os
import re
import html

def clean_release_notes(raw: str) -> str:
    # 1. Remove HTML img tags (<img ... /> or <img ...>)
    text = re.sub(r"<img\b[^>]*\/?>", "", raw, flags=re.IGNORECASE)

    # 2. Remove layout/header HTML tags (<details>, <summary>, <h1-6>, <p>, <div>, <span>, <align>)
    text = re.sub(r"</?(?:details|summary|h[1-6]|p|div|span|align)\b[^>]*>", "", text, flags=re.IGNORECASE)

    # 3. Remove Markdown image tags ![alt](url)
    text = re.sub(r"!\[.*?\]\(.*?\)", "", text)

    # 4. Transform PR lines to "- #123 by @author"
    cleaned_lines = []
    for line in text.splitlines():
        pr_match = re.search(r"/pull/(\d+)", line)
        if pr_match:
            pr_num = pr_match.group(1)
            users = re.findall(r"@[A-Za-z0-9_-]+", line)
            author = None
            for u in users:
                if u.lower() != "@github-actions":
                    author = u
                    break
            if not author and users:
                author = users[0]

            if author:
                cleaned_lines.append(f"- #{pr_num} by {author}")
            else:
                cleaned_lines.append(f"- #{pr_num}")
        else:
            cleaned_lines.append(line)

    text = "\n".join(cleaned_lines)

    # 5. Strip leading markdown header symbols (### Header -> Header)
    text = re.sub(r"^#+\s*", "", text, flags=re.MULTILINE)

    # 6. Convert bullet lists starting with "* " to "- "
    text = re.sub(r"^\*\s+", "- ", text, flags=re.MULTILINE)

    # 7. Compress 3 or more consecutive blank lines into a single blank line
    text = re.sub(r"\n\s*\n\s*\n+", "\n\n", text).strip()

    # 8. Escape HTML special characters for Telegram parse_mode: HTML
    text = html.escape(text)

    # 9. Truncate safely at 3800 chars to avoid Telegram message length limit (4096)
    return text[:3800]

if __name__ == "__main__":
    raw_body = os.environ.get("RAW_BODY", "")
    print(clean_release_notes(raw_body))
