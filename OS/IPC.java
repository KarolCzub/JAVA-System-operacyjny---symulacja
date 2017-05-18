package OS;
public class IPC {
	public static FAT8 fat;
	private static int counter=1;
	public static void wyslijKomunikat(int PID,int od_kogo, String komunikat) {
		PCB proces = Zarzadzanie_procesami.zwroc_proces(PID);
		if(proces==null){
			System.out.println("Nie ma takiego procesu");
			return;
		}
		else if (proces.stan == 3) proces.semafor.signal(); // jesli proces jest wstrzymany
		proces.msgqueue.insert(od_kogo, komunikat);
		System.out.println("Proces " + PID + ": dodano komunikat");
	}
	public static String odbierzKomunikat(int PID) {
		PCB proces = Zarzadzanie_procesami.zwroc_proces(PID);
		Message result = proces.msgqueue.remove();
		String filename="";
		if (result == null){ proces.semafor.waits(proces);
		return null;
		}
		else {
			 filename="msg"+PID+"_"+counter;
			fat.create_file(filename);
			counter++;
			fat.write_to_file(filename, result.PID+" "+result.msg);
			fat.close_file();
			System.out.println("Proces " + PID + ": odebrano komunikat: " + result.msg);
			System.out.println(filename);
		}
		return filename;
	}
	public static void wyswietlKolejke(int PID) {
		PCB proces = Zarzadzanie_procesami.zwroc_proces(PID);
		if(proces==null)
			{
			System.out.println("Nie ma takiego procesu");
			return;
			}
		System.out.println(proces.msgqueue.print());
	}
};