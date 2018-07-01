package com.feliz.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片找茬
 */
public class FindDefferentOfPictures {

    public static void main(String[] args) throws IOException {
        new FindDefferentOfPictures().compare("src/images/1.png","src/images/3.png");
    }
    //在不同的周围画矩形
    public void  drawRectangle(int minX, int minY, int maxX, int maxY, BufferedImage image, String outPath) throws IOException {
        int morespace = 20;
        drawHorizontalLine(minX-morespace, maxX+morespace, minY-morespace, image);
        drawHorizontalLine(minX-morespace, maxX+morespace, maxY+morespace, image);
        drawVerticalLine(minY-morespace, maxY+morespace, minX-morespace, image);
        drawVerticalLine(minY-morespace, maxY+morespace, maxX+morespace, image);
//        File out = new File(outPath);
//        ImageIO.write(image, "png", out);
    }
    //矩形中水平线
    public void drawHorizontalLine(int x1, int x2, int y, BufferedImage image) {
        int lineWidth = 5;
        int start = (x1<x2)?x1:x2;
        int end = (x1>x2)?x1:x2;
        for (int i = x1; i < x2; i++) {
            for (int j = y; j < y+lineWidth; j++) {
                image.setRGB(i, j, 0xFFFF0000);
            }
        }
    }
    //矩形中垂直线
    public void drawVerticalLine(int y1, int y2, int x, BufferedImage image) {
        int lineWidth = 5;
        int start = (y1<y2)?y1:y2;
        int end = (y1>y2)?y1:y2;
        for (int i = y1; i < y2; i++) {
            for (int j = x; j <= x+lineWidth; j++) {
                image.setRGB(j, i,0xFFFF0000);
            }
        }
    }
    //比较图片
    public String compare(String pathA, String pathB) throws IOException {
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

        Position firstPos = positions.get(0);
        Position pos, nextPos;
        for (int i = 0; i < positions.size(); i++) {

            pos = positions.get(i);
            System.out.println(pos.x+ ","+pos.y);
            if (i == positions.size()-1) {
                drawRectangle(firstPos.x, firstPos.y, pos.x, pos.y, imageB, "src/images/out.png");
            }else {
                nextPos = positions.get(i+1);
                if (Math.abs(pos.x - nextPos.x) >100 || Math.abs(pos.y - nextPos.y) >100) {
                    drawRectangle(firstPos.x, firstPos.y, pos.x, pos.y, imageB, "src/images/out.png");
                    firstPos = nextPos;
                }
            }


        }
        String outPath = "src/images/out.png";
        File out = new File(outPath);
        ImageIO.write(imageB, "png", out);
        return outPath;
    }
    //rgb比较
    public boolean compareRGB(int r1, int r2) {
        final int FAULT = 30;
        int red1 = (r1&0xff0000)>>16;
        int green1 = (r1&0x00ff00)>>8;
        int blue1 = (r1&0x0000ff);
        int red2 = (r2&0xff0000)>>16;
        int green2 = (r2&0x00ff00)>>8;
        int blue2 = (r2&0x0000ff);
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
