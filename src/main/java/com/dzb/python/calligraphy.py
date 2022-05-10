# encoding: utf-8
#!/usr/bin/env python
#######################
#杜正波

#######################
import cv2
import os
import numpy as np
from skimage import morphology
from functools import reduce
from PIL import Image
import math

def perdict_img(img2_path,img_path):
    # 输入部分
    img1 = cv2.imread(img_path, cv2.IMREAD_GRAYSCALE)
    # 输入学生模仿的字的图片
    img2 = cv2.imread(img2_path, cv2.IMREAD_GRAYSCALE)

    result = {}
    ###################################################################################
    # 一、图片预处理部分（去除墨块、主体提取、高精度配准）
    width = 400
    height1 = int(width * img1.shape[0] / img1.shape[1])
    height2 = int(width * img2.shape[0] / img2.shape[1])
    img1 = cv2.resize(img1, (width, height1), interpolation=cv2.INTER_AREA)
    img2 = cv2.resize(img2, (width, height2), interpolation=cv2.INTER_AREA)

    # 二值化与去除墨块
    def num2true(img):
        img = np.array(img).astype(bool)
        for i in range(img.shape[0]):
            for j in range(img.shape[1]):
                if img[i][j] == 0:
                    img[i][j] = bool(True)
                else:
                    img[i][j] = bool(False)
        return img

    def true2num(img):
        img.dtype = 'uint8'
        for i in range(img.shape[0]):
            for j in range(img.shape[1]):
                if img[i][j] == 1:
                    img[i][j] = 0
                else:
                    img[i][j] = 255
        return img

    _, res1 = cv2.threshold(img1, 90, 255, cv2.THRESH_BINARY)
    _, res2 = cv2.threshold(img2, 90, 255, cv2.THRESH_BINARY)
    # 将图像进行二值化处理
    res1 = num2true(res1)
    res2 = num2true(res2)
    # 进行阈值处理，阈值未
    res1 = morphology.remove_small_objects(res1, min_size=400, connectivity=1)
    res2 = morphology.remove_small_objects(res2, min_size=400, connectivity=1)
    # 进行去磨块处理
    res1 = true2num(res1)
    res2 = true2num(res2)
    image1 = res1.copy()
    image2 = res2.copy()

    # 取中心点函数
    def center(location):
        lenth = location.shape[0]
        sumloc = 0
        for i in range(lenth):
            sumloc = sumloc + location[i, 0, 0]
        avrlenth = sumloc / lenth
        sumlocw = 0
        for i in range(lenth):
            sumlocw = sumlocw + location[i, 0, 1]
        avrwidth = sumlocw / lenth
        return avrlenth, avrwidth

    # 将不连通的图提取外接矩形
    def trans2whole(image):
        # image = 255 - image
        minx, miny, maxx, maxy = width, width, 0, 0
        for i in range(image.shape[0]):
            for j in range(image.shape[1]):
                if image[i][j] == 0:
                    if i <= minx:
                        minx = i
                    if i >= maxx:
                        maxx = i
                    if j <= miny:
                        miny = j
                    if j >= maxy:
                        maxy = j
        ptLeftTop = (miny, minx)
        ptRightBottom = (maxy, maxx)
        point_color = (0, 0, 0)  # BGR
        return ptLeftTop, ptRightBottom

    # 提取字体最小拟合矩形
    def minrect(image):
        ptLeftTop, ptRightBottom = trans2whole(image)
        roi = image[ptLeftTop[1]:ptRightBottom[1], ptLeftTop[0]:ptRightBottom[0]]
        return roi

    newimage = np.zeros((400, 400), np.uint8)
    newimage.fill(255)

    # 图像平移配准
    def imgmatch(newimage, src, cx, cy):
        cx1 = cy1 = int(newimage.shape[0] / 2)
        deltax = cx1 - cx
        deltay = cy1 - cy
        # delta为提取图像中心点与200，200的差距，然后将学生图像平移至新的图层上
        for i in range(src.shape[0]):
            for j in range(src.shape[1]):
                if i + deltax < newimage.shape[0] and j + deltay < newimage.shape[0]:
                    newimage[i + deltax][j + deltay] = src[i][j]
        return newimage

    # 新建图像重新匹配
    def newimage(model, img, size):
        newimage1 = np.zeros((size, size), np.uint8)
        newimage2 = np.zeros((size, size), np.uint8)
        newimage1.fill(255)
        newimage2.fill(255)
        # model为模板字帖，img为学生字帖，求其最小矩阵
        roi1 = minrect(model)
        roi2 = minrect(img)
        # 计算图像空间矩，求质心
        M1 = cv2.moments(255 - roi1)
        M2 = cv2.moments(255 - roi2)
        num_model = int(M1["m00"]) / 255
        num_roi = int(M2["m00"]) / 255
        scale_percent = math.sqrt(num_model / num_roi)
        roi2 = cv2.resize(roi2, (int(roi2.shape[1] * scale_percent), int(roi2.shape[0] * scale_percent)),
                          interpolation=cv2.INTER_AREA)
        M2 = cv2.moments(255 - roi2)
        # 重心计算
        cx1 = int(M1['m10'] / M1['m00'])
        cy1 = int(M1['m01'] / M1['m00'])
        cx2 = int(M2['m10'] / M1['m00'])
        cy2 = int(M2['m01'] / M1['m00'])
        newimage1 = imgmatch(newimage1, roi1, cy1, cx1)
        newimage2 = imgmatch(newimage2, roi2, cy2, cx2)
        return newimage1, newimage2

    newimage1, newimage2 = newimage(image1, image2, 400)

    strumodel1 = 255 - newimage1.copy()
    strumodel2 = 255 - newimage1.copy()
    strumodel3 = 255 - newimage1.copy()
    strumodel4 = 255 - newimage1.copy()
    strustudent1 = 255 - newimage2.copy()
    strustudent2 = 255 - newimage2.copy()
    strustudent3 = 255 - newimage2.copy()
    strustudent4 = 255 - newimage2.copy()

    #########################################################################################
    # 二、字形笔画IOU评价
    # 计算IOU
    def get_iou(target, prediction):
        intersection = np.logical_and(target, prediction)  # 逻辑与，下面为逻辑或，求其交并比
        union = np.logical_or(target, prediction)
        iou_score = np.sum(intersection) / np.sum(union)
        return iou_score

    iou1 = get_iou(255 - newimage1, 255 - newimage2)
    score1 = iou1 * 10 * (2 - iou1)
    result[0] = score1
    imageshow1 = newimage1.copy()
    imageshow2 = newimage2.copy()
    imageshow2 = Image.fromarray(imageshow2).convert("RGB")
    imageshow2 = np.array(imageshow2)
    contours, hier = cv2.findContours(255 - newimage1, cv2.RETR_CCOMP, cv2.CHAIN_APPROX_SIMPLE)
    imageshow2 = 255 - imageshow2
    cv2.drawContours(imageshow2, contours, -1, (0, 0, 255), 3)

    #########################################################################################
    # 三、框架结构IOU评价
    imagestr = imageshow2.copy()
    thresh1 = 255 - newimage1
    thresh2 = 255 - newimage2

    contours1, hierarchy1 = cv2.findContours(thresh1, 3, 2)  # 模仿
    contours2, hierarchy2 = cv2.findContours(thresh2, 3, 2)  # 原图
    centerdot1 = np.zeros(shape=(len(contours1), 1, 2), dtype=np.int)
    centerdot2 = np.zeros(shape=(len(contours2), 1, 2), dtype=np.int)

    for i in range(len(contours1)):
        # 1.先找到轮廓
        cnt = contours1[i]
        # 2.寻找凸包，得到凸包的角点
        hull = cv2.convexHull(cnt)
        # 3.绘制凸包
        # print(hull)
        cv2.polylines(res1, [hull], True, (0, 0, 0), 2)
        centerdot1[i, 0, 0] = hull[0, 0, 0]  # int(center(hull)[0])
        centerdot1[i, 0, 1] = hull[0, 0, 1]  # int(center(hull)[1])
        # print(centerdot1[i, 0, 0])

    for i in range(len(contours2)):
        # 1.先找到轮廓
        cnt = contours2[i]
        # 2.寻找凸包，得到凸包的角点
        hull = cv2.convexHull(cnt)
        # 3.绘制凸包
        # print(hull)
        cv2.polylines(res2, [hull], True, (0, 0, 0), 2)
        centerdot2[i, 0, 0] = hull[0, 0, 0]
        centerdot2[i, 0, 1] = hull[0, 0, 1]
        # print(centerdot2[i, 0, 0])

    cv2.polylines(thresh1, [centerdot1], True, (255, 255, 255), 2)
    cv2.polylines(thresh2, [centerdot2], True, (255, 255, 255), 2)

    # 画外部轮廓_, thresh3 = cv2.threshold(image1, 1, 255, cv2.THRESH_BINARY + cv2.THRESH_OTSU)
    contours3, hierarchy3 = cv2.findContours(thresh1, 3, 2)
    contours4, hierarchy4 = cv2.findContours(thresh2, 3, 2)

    # 1.先找到轮廓
    cnt3 = contours3[0]
    cnt4 = contours4[0]
    # 2.寻找凸包，得到凸包的角点
    hull3 = cv2.convexHull(cnt3)
    hull4 = cv2.convexHull(cnt4)
    # 3.绘制凸包
    cv2.polylines(thresh1, [hull3], True, (255, 255, 255), 1)
    cv2.polylines(imagestr, [hull3], True, (0, 0, 255), 6)  # 可视化
    cv2.polylines(imagestr, [hull4], True, (255, 0, 0), 3)  # 可视化
    cv2.polylines(thresh2, [hull4], True, (255, 255, 255), 1)
    cv2.fillPoly(thresh1, [hull3], (255, 255, 255))  # 填充内部
    cv2.fillPoly(thresh2, [hull4], (255, 255, 255))  # 填充内部

    # 计算IOU
    iou2 = get_iou(thresh1, thresh2)
    score2 = iou2 * 10
    # print("--------------PART 2---------------：字体框架结构评分为", score2)
    result[1] = score2

    ######################################################################################################
    # 三、字体的骨架评分
    def Zhang_Suen_thining(img):
        # get shape
        H, W = img.shape
        # prepare out image
        out = np.zeros((H, W), dtype=np.int)
        # 将图像二值化，非0即1
        out[img[...] > 0] = 1
        # inverse，0和1反转，0为白、1为黑
        out = 1 - out
        out = out.astype(np.uint8) * 255
        return out

    # Read image
    bone1 = newimage1.copy()
    bone2 = newimage2.copy()
    out1 = Zhang_Suen_thining(255 - bone1)
    out2 = Zhang_Suen_thining(255 - bone2)
    bone11 = Image.fromarray(out1)
    bone21 = Image.fromarray(out2)

    # 计算相似度
    # 计算图片的局部哈希值--pHash
    def ORB_img_similarity(img1_path, img2_path):
        """
        :param img1_path: 图片1路径
        :param img2_path: 图片2路径
        :return: 图片相似度
        """
        try:
            # 读取图片
            img1 = img1_path
            img2 = img2_path

            # 初始化ORB检测器
            orb = cv2.ORB_create()
            kp1, des1 = orb.detectAndCompute(img1, None)
            kp2, des2 = orb.detectAndCompute(img2, None)

            # 提取并计算特征点
            bf = cv2.BFMatcher(cv2.NORM_HAMMING)
            # knn筛选结果
            matches = bf.knnMatch(des1, trainDescriptors=des2, k=2)

            # 查看最大匹配点数目
            good = [m for (m, n) in matches if m.distance < 0.75 * n.distance]
            similary = len(good) / len(matches)
            return similary

        except:
            return '0'

    def phash(img):
        """
        :param img: 图片
        :return: 返回图片的局部hash值
        """
        img = img.resize((8, 8), Image.ANTIALIAS).convert('L')
        avg = reduce(lambda x, y: x + y, img.getdata()) / 64.
        hash_value = reduce(lambda x, y: x | (y[1] << y[0]),
                            enumerate(map(lambda i: 0 if i < avg else 1, img.getdata())), 0)
        return hash_value

    # 计算两个图片相似度函数局部敏感哈希算法
    def phash_img_similarity(img1_path, img2_path):
        """
        :param img1_path: 图片1路径
        :param img2_path: 图片2路径
        :return: 图片相似度
        """
        # 读取图片
        img1 = img1_path
        img2 = img2_path

        # 计算汉明距离
        distance = bin(phash(img1) ^ phash(img2)).count('1')
        similary = 1 - distance / max(len(bin(phash(img1))), len(bin(phash(img1))))
        return similary

    similar1 = ORB_img_similarity(out1, out2)
    similar2 = phash_img_similarity(bone11, bone21)
    score3 = math.cos((similar1 - 1)) * 5 + similar2 * 5
    # print("--------------PART 3---------------：骨架的相似度为", score3)
    result[2] = score3

    ########################################################################
    # 计算米字格特征
    # 旋转图像
    def rotate_bound(image, angle):
        # 获取图像的尺寸
        # 旋转中心
        (h, w) = image.shape[:2]
        (cx, cy) = (w / 2, h / 2)

        # 设置旋转矩阵
        M = cv2.getRotationMatrix2D((cx, cy), -angle, 1.0)
        cos = np.abs(M[0, 0])
        sin = np.abs(M[0, 1])

        # 计算图像旋转后的新边界
        nW = int((h * sin) + (w * cos))
        nH = int((h * cos) + (w * sin))

        # 调整旋转矩阵的移动距离（t_{x}, t_{y}）
        M[0, 2] += (nW / 2) - cx
        M[1, 2] += (nH / 2) - cy

        return cv2.warpAffine(image, M, (nW, nH))

    # 计算投影直方图
    def statisticalhis(img):
        h = img.shape[0]
        w = img.shape[1]

        # 从列看
        a = np.zeros([w, 1], np.float32)

        for j in range(0, w):  # 遍历每一行
            for i in range(0, h):  # 遍历每一列
                if img[i, j] == 255:  # 判断该点是否为黑点，0代表黑点
                    a[j, 0] += 1
                    img[i, j] = 0  # 将其改为白点，即等于255
        for j in range(0, w):  # 遍历每一行
            for i in range(0, int(a[j, 0])):  # 从该行应该变黑的最左边的点开始向最右边的点设置黑点
                img[h - i - 1, j] = 255  # 设置黑点
        return img, a

    strumodel1, amodle1 = statisticalhis(rotate_bound(strumodel1, 1))
    strustudent1, astudent1 = statisticalhis(rotate_bound(strustudent1, 1))
    iou41 = get_iou(strumodel1, strustudent1)

    strumodel2, amodle2 = statisticalhis(rotate_bound(strumodel2, -45))
    strustudent2, astudent2 = statisticalhis(rotate_bound(strustudent2, -45))
    iou42 = get_iou(strumodel2, strustudent2)

    strumodel3, amodle3 = statisticalhis(rotate_bound(strumodel3, 45))
    strustudent3, astudent3 = statisticalhis(rotate_bound(strustudent3, 45))
    iou43 = get_iou(strumodel3, strustudent3)

    strumodel4, amodle4 = statisticalhis(rotate_bound(strumodel4, 91))
    strustudent4, astudent4 = statisticalhis(rotate_bound(strustudent4, 91))
    iou44 = get_iou(strumodel4, strustudent4)

    result[3] = iou41
    result[4] = iou42
    result[5] = iou43
    result[6] = iou44

    # 计算总分
    tot = score1 * 0.4 + score2 * 0.3 + score3 * 0.3
    total = 1.94064354 * iou1 + 6.51982931 * iou2 - 0.96069589 * similar1 + 4.23679642 * similar2 - 2.637720298361252

    result[7] = tot
    result[8] = total

    return result

import sys

if __name__ == '__main__':
    print(perdict_img(sys.argv[1],sys.argv[2]))
