# 验证码图片生成器 (跨平台，支持倾斜，高对比度，可调垂直偏移)
# 基于 jinyaoMa 的原始代码修改和维护
# 修改亮度对比度，增加垂直偏移参数，优化字符位置计算，增强边界保护，适配 Python 3
#
# @author SuiMu

import os
import platform
from PIL import Image, ImageDraw, ImageFont, ImageFilter
import random
import string


def _get_default_font_path():
    """自动查找系统可用字体"""
    system = platform.system()
    candidates = []

    if system == "Windows":
        windir = os.environ.get("WINDIR", "C:\\Windows")
        candidates = [
            os.path.join(windir, "Fonts", "arial.ttf"),
            os.path.join(windir, "Fonts", "calibri.ttf"),
            os.path.join(windir, "Fonts", "times.ttf"),
        ]
    elif system == "Darwin":
        candidates = [
            "/System/Library/Fonts/Helvetica.ttc",
            "/System/Library/Fonts/Arial.ttf",
        ]
    else:
        candidates = [
            "/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf",
            "/usr/share/fonts/truetype/liberation/LiberationSans-Regular.ttf",
        ]

    for f in candidates:
        if os.path.isfile(f):
            return f
    return None


class VCodeGenerator(object):
    def __init__(self, fontPath=None, numOfChars=4, height=64):
        if fontPath is None:
            fontPath = _get_default_font_path()
            if fontPath is None:
                raise RuntimeError(
                    "找不到可用字体，请手动指定 fontPath。\n"
                    "Windows: 'C:\\\\Windows\\\\Fonts\\\\arial.ttf'\n"
                    "macOS: '/System/Library/Fonts/Helvetica.ttc'\n"
                    "Linux: '/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf'"
                )
        self.__strFontPath = fontPath
        self.__intNumOfChars = numOfChars
        self.__intHeight = height
        self.__intWidth = height * numOfChars
        self.__dblFontSize = 0.75
        self.__dblMaxRotation = 30
        self.__vertical_offset = 10   # 垂直偏移：数值越大字符越靠上
        self.__imgVerificationCode = None

    def setFontSize(self, fontSize):
        if not (0.0 < fontSize <= 1.0):
            raise ValueError("fontSize 必须在 0 到 1 之间")
        self.__dblFontSize = fontSize
        return self

    def getFontSize(self):
        return self.__dblFontSize

    def setMaxRotation(self, degrees):
        self.__dblMaxRotation = degrees
        return self

    def setVerticalOffset(self, offset):
        self.__vertical_offset = offset
        return self

    def __random_bg_color(self):
        return (random.randint(200, 255),
                random.randint(200, 255),
                random.randint(200, 255))

    def __random_font_color(self):
        return (random.randint(0, 128),
                random.randint(0, 128),
                random.randint(0, 128))

    def __random_char(self):
        chars = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz"
        return random.choice(chars)

    def generate(self, save_path='code.jpg'):
        # 1. 背景底图
        bg_color = self.__random_bg_color()
        self.__imgVerificationCode = Image.new('RGB', (self.__intWidth, self.__intHeight), bg_color)

        # 2. 背景噪点
        draw = ImageDraw.Draw(self.__imgVerificationCode)
        for _ in range(int(self.__intWidth * self.__intHeight * 0.3)):
            x = random.randint(0, self.__intWidth - 1)
            y = random.randint(0, self.__intHeight - 1)
            noise_color = (random.randint(180, 240),
                           random.randint(180, 240),
                           random.randint(180, 240))
            draw.point((x, y), fill=noise_color)

        # 3. 干扰线（保留）
        for _ in range(2):
            x1 = random.randint(0, self.__intWidth // 2)
            y1 = random.randint(0, self.__intHeight)
            x2 = random.randint(self.__intWidth // 2, self.__intWidth)
            y2 = random.randint(0, self.__intHeight)
            draw.line([(x1, y1), (x2, y2)], fill=(180, 180, 180), width=2)

        # 4. 逐个字符绘制并旋转（旋转前上移）
        font_size = int(self.__intHeight * self.__dblFontSize)
        font = ImageFont.truetype(self.__strFontPath, font_size)
        margin = self.__intHeight * (1 - self.__dblFontSize)
        code = ""

        for i in range(self.__intNumOfChars):
            char = self.__random_char()

            # 测量字符实际大小
            temp_img = Image.new('RGBA', (1, 1), (0, 0, 0, 0))
            temp_draw = ImageDraw.Draw(temp_img)
            bbox = temp_draw.textbbox((0, 0), char, font=font)
            text_w = bbox[2] - bbox[0]
            text_h = bbox[3] - bbox[1]

            # **************** 关键修改 ****************
            # 为了让字符在旋转前就上移，我们加大画布高度，并在绘制时直接应用偏移
            pad = 15
            extra = abs(self.__vertical_offset) * 2   # 预留足够空间
            canvas_size_w = text_w + pad * 2
            canvas_size_h = text_h + pad 
            canvas_size = max(canvas_size_w, canvas_size_h)  
            # *******************************************

            char_img = Image.new('RGBA', (canvas_size, canvas_size), (0, 0, 0, 0))
            char_draw = ImageDraw.Draw(char_img)

            # 水平居中
            x_offset = (canvas_size - text_w) // 2
            # 垂直时应用偏移：这里减去 self.__vertical_offset 即可上移字符
            y_offset = (canvas_size - text_h) // 2 - self.__vertical_offset

            font_color = self.__random_font_color()
            char_draw.text((x_offset, y_offset), char, font=font, fill=font_color + (255,))

            # 旋转
            angle = random.uniform(-self.__dblMaxRotation, self.__dblMaxRotation)
            rotated_char = char_img.rotate(angle, expand=True, fillcolor=(0, 0, 0, 0))

            # 粘贴时不再减去 vertical_offset，因为偏移已经在画布内完成
            slot_center_x = int(self.__intHeight * i + margin + self.__intHeight / 2)
            paste_x = slot_center_x - rotated_char.width // 2
            paste_y = (self.__intHeight - rotated_char.height) // 2

            # 边界保护（确保不出图）
            paste_x = max(0, min(paste_x, self.__intWidth - rotated_char.width))
            paste_y = max(0, min(paste_y, self.__intHeight - rotated_char.height))

            self.__imgVerificationCode.paste(rotated_char, (paste_x, paste_y), rotated_char)
            code += char

        # 5. 轻微模糊并保存
        self.__imgVerificationCode = self.__imgVerificationCode.filter(ImageFilter.SMOOTH)
        self.__imgVerificationCode.save(save_path, 'jpeg', quality=85)
        return code