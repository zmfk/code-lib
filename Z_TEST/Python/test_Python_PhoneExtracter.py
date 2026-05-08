import sys
import os

# 把 Python 子目录加入搜索路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '../../Python'))

from PhoneExtracter import PhoneExtracter

# /path/to/your/file.html替换为要查询的URL 或本地文件路径
test_path = f"/path/to/your/file.html".replace("\\", "/")

extractor = PhoneExtracter(
    urlAddress=[test_path],
    phoneFormat=None   # 使用默认的中国号码正则
)

results = extractor.extract()
for url, phones in results.items():
    print(f"从 {url} 提取到的电话号码：")
    for phone in phones:
        print(f"  - {phone}")