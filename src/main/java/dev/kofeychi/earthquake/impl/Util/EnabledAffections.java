package dev.kofeychi.earthquake.impl.Util;

public class EnabledAffections {
    public boolean RotX,PosX;
    public boolean RotY,PosY;
    public boolean RotZ,PosZ;

    public EnabledAffections(String encoded){
        RotX = Char(encoded.charAt(0));
        RotY = Char(encoded.charAt(1));
        RotZ = Char(encoded.charAt(2));
        PosX = Char(encoded.charAt(3));
        PosY = Char(encoded.charAt(4));
        PosZ = Char(encoded.charAt(5));
    }
    public EnabledAffections(
            boolean RotationX,
            boolean RotationY,
            boolean RotationZ,
            boolean PositionX,
            boolean PositionY,
            boolean PositionZ
    ){
        RotX = RotationX;
        RotY = RotationY;
        RotZ = RotationZ;
        PosX = PositionX;
        PosY = PositionY;
        PosZ = PositionZ;
    }
    public static boolean Char(Character character){
        return character.equals('y');
    }
    public static char Char(boolean b){
        return b ? 'y' : 'n';
    }

    @Override
    public String toString() {
        return ""+Char(RotX)+Char(RotY)+Char(RotZ)+Char(PosX)+Char(PosY)+Char(PosZ);
    }
}
