package com.feliz.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Feliz on 2017/7/28.
 * 图片找不同，还有好多问题
 */
public class FindDefferentOfPictures {

    public static void main(String[] args) throws IOException {
        new FindDefferentOfPictures().compare("3.jpg","1.jpg");
    }
//在不同的周围画矩形
    public void  drawRectangle(int minX, int minY, int maxX, int maxY, BufferedImage image, String outPath) throws IOException {
        int morespace = 20;
        drawHorizontalLine(minX-morespace, maxX+morespace, minY-morespace, image);
        drawHorizontalLine(minX-morespace, maxX+morespace, maxY+morespace, image);
        drawVerticalLine(minY-morespace, maxY+morespace, minX-morespace, image);
        drawVerticalLine(minY-morespace, maxY+morespace, maxX+morespace, image);
        File out = new File(outPath);
        ImageIO.write(image, "jpg", out);
    }
//矩形中水平线
    public void drawHorizontalLine(int x1, int x2, int y, BufferedImage image) {
        int lineWidth = 5;
        int start = (x1<x2)?x1:x2;
        int end = (x1>x2)?x1:x2;
        for (int i = x1; i < x2; i++) {
            for (int j = y; j < y+lineWidth; j++) {
                image.setRGB(i, j, 0xff0000);
            }
        }
    }
//矩形中垂直线
    public void drawVerticalLine(int y1, int y2, int x, BufferedImage image) {
        int lineWidth = 5;
        int start = (y1<y2)?y1:y2;
        int end = (y1>y2)?y1:y2;
        for (int i = y1; i < y2; i++) {
            for (int j = x; j < x+lineWidth; j++) {
                image.setRGB(j, i,0xff0000);
            }
        }
    }
//比较图片
    public void compare(String pathA, String pathB) throws IOException {
        List<Position> positions = new ArrayList<>();
        File file = new File(pathA);
        File file2 = new File(pathB);
        BufferedImage imageA;
        BufferedImage imageB;
        imageA = ImageIO.read(file);
        imageB = ImageIO.read(file2);
        int width = imageA.getWidth();
        int height = imageA.getHeight();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int rgbA = imageA.getRGB(i, j);
                int rgbB = imageB.getRGB(i, j);
                if (compareRGB(rgbA, rgbB)) {
                    Position position = new Position(i, j);
                    positions.add(position);
                }
            }
        }
        int maxX = positions.get(0).x;
        int maxY = positions.get(0).y;
        int minX = positions.get(0).x;
        int minY = positions.get(0).y;
        Iterator<Position> iterator = positions.iterator();
        while (iterator.hasNext()) {
            Position pos = iterator.next();
            System.out.println("("+pos.x+","+pos.y+")");
            if (pos.x <= minX) {
                minX = pos.x;
            }
            if (pos.y <= minY) {
                minY = pos.y;
            }
            if (pos.x >= minX) {
                maxX = pos.x;
            }
            if (pos.y >= minY) {
                maxY = pos.y;
            }
        }
        drawRectangle(minX, minY, maxX, maxY, imageB, "out.jpg");
    }
//rgb比较
    public boolean compareRGB(int r1, int r2) {
        final int FAULT = 30;
        int red1 = (r1&0xff0000)>>16;
        int green1 = (r1&0xff0000)>>8;
        int blue1 = (r1&0xff0000);
        int red2 = (r2&0xff0000)>>16;
        int green2 = (r2&0xff0000)>>8;
        int blue2 = (r2&0xff0000);
        if (Math.abs(red1- red2)< FAULT&&Math.abs(green1- green2)< FAULT&&Math.abs(blue1- blue2)< FAULT) {
            return false;
        }else return true;
    }

//位置属性
    private class Position {
        int x;
        int y;
        public Position(int x, int y) {
            this.x = x;
            this.y =y;
        }
    }
}
