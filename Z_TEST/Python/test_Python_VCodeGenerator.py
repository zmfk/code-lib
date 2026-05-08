# test_Python_VCodeGenerator.py
import sys
import os

# 把 Python 子目录加入搜索路径，方便导入
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '../../Python'))

from VCodeGenerator import VCodeGenerator

if __name__ == "__main__":
    # 请根据你的系统字体路径修改，Windows 常用字体如 Arial, calibri, simhei 等
    font_path = "C:\\Windows\\Fonts\\Arial.ttf"
    
    # 创建生成器：4个字符，高度80像素
    generator = VCodeGenerator(fontPath=font_path, numOfChars=4, height=80)
    
    # 可选：设置字体大小比例
    generator.setFontSize(0.75)
    
    # 在当前路径下生成验证码图片，保存为 test_code.jpg
    code = generator.generate(
        os.path.join(os.path.dirname(__file__), "test_code.jpg")
    )
    
    print(f"生成的验证码字符串: {code}")
    print("图片已保存为 test_code.jpg")