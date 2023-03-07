package ru.netology.graphics.image;

public class Schema implements TextColorSchema{
    private final int COLORSNUMBER = 256;
    @Override
    public char convert(int color) {
        char [] chars = {'#', '$', '@', '%', '*', '+', '-', '.'};
        return chars[(color/(COLORSNUMBER/ chars.length))];
    }
}
