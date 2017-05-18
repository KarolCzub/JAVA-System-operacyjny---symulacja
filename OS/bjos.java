package OS;

public class bjos {

	public static void main(String[] args) {
		FAT8 fat8 = new FAT8();
		procesor p1=new procesor();
		IPC.fat=fat8;
		Zarzadzanie_procesami zarzadzanie_procesami = new Zarzadzanie_procesami(p1);
        zarzadzanie_pam RAM=new zarzadzanie_pam(fat8);
		Interpreter1 interpreter = new Interpreter1(fat8, zarzadzanie_procesami,RAM);
		Shell shell = new Shell(fat8, p1, zarzadzanie_procesami, RAM, interpreter);
		shell.installPrograms();
		shell.SIEMANO_KOLANO();
		shell.shellStart();
	

	}

}

