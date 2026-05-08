# 电话号码提取器 (中国号码格式 + 400/800 + 特服号码)
# 基于 jinyaoMa 的原始代码修改，适配 Python 3 及国内号码格式

import urllib.request as req
import re

class PhoneExtracter(object):
    """
    从网页内容中提取符合中国号码格式的电话号码
    默认支持：手机号（含空格/连字符）、固话（含空格/连字符）、400/800热线、特服号码
    """

    def __init__(self, urlAddress=None, phoneFormat=None):
        self.__url = urlAddress or []
        self.__phoneRegex = phoneFormat or [
            # 手机号：允许中间有空格或连字符，如 189 1750 1433 或 189-1750-1433
            r"\b1[3-9]\d[\s-]?\d{4}[\s-]?\d{4}\b",
            # 固话：区号(3-4位)带空格或连字符，号码部分也可能有空格，如 021-3535 0722
            r"\b0[1-9]\d{1,2}[\s-]?\d{3,4}[\s-]?\d{4}\b",
            # 400/800 热线，如 400-618-1518
            r"\b[48]00[\s-]?\d{3}[\s-]?\d{4}\b",
            # 企业客服号 (5位，95/96开头，如 95152、95598)
            r"\b9[56]\d{3}\b",
            # 公共服务特服号码白名单（110, 114, 120, 122, 123xx 等）
            r"\b(?:1(?:1[0249]|20|22)|123(?:[135689]\d?|33|45|48|55|58|65|66|69)|1212[12]|12580|12122|12320|95598|12336|12369|12358|12309|12329|12395|12365|12338|999)\b"
        ]

    def extract(self):
        """
        提取匹配正则表达式的电话号码
        @return 字典，键为 URL，值为该页面提取到的电话号码列表（去重、排序）
        """
        listOfPhones = {}
        for urlAddr in self.__url:
            try:
                cnt = req.Request(urlAddr, headers={
                    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
                })
                page = req.urlopen(cnt)
                data = page.read().decode("utf-8")
                
                # 清除 HTML 标签，只保留纯文本
                clean_data = re.sub(r'<[^>]+>', '', data)
                
                tempSet = set()
                for phoneFormat in self.__phoneRegex:
                    for match in re.finditer(phoneFormat, clean_data):
                        num = match.group().strip()
                        # 长度最终防线：去除所有分隔符后，号码长度应在3-12位数字之间
                        digits = re.sub(r'\D', '', num)
                        if 3 <= len(digits) <= 12:
                            tempSet.add(num)
                listOfPhones[urlAddr] = sorted(tempSet, key=lambda x: (len(x), x))
            except Exception as e:
                print(f"无法访问 {urlAddr}：{e}")
        return listOfPhones