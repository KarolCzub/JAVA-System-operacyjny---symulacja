package OS;
import java.io.*;
import java.lang.Object;
import java.util.LinkedList;
import java.util.Scanner;
public class Interpreter1 
{
	private int name;
	private int RejestrA;	
	private int RejestrB;
	private int RejestrC;
	private int licznik_rozkazow;
	private int adres = 1;
	private int flaga;
	private FAT8 fat;
	private Zarzadzanie_procesami procesami;
	private zarzadzanie_pam ram;
	private locks zamek;
	public LinkedList<locks> kolejka_zamkow;
	Scanner scanner = new Scanner(System.in);
	public Interpreter1(FAT8 fat1,Zarzadzanie_procesami id,zarzadzanie_pam pamiec)
	{
		ram = pamiec;
		procesami=id;
		fat= fat1;
		RejestrA=RejestrB=RejestrC=licznik_rozkazow=0;
		adres=0;
		kolejka_zamkow = new LinkedList<locks>();
	}
	public void PobierzRejestry(PCB rozkaz)
	{
		RejestrA=rozkaz.rejestr_A;
		RejestrB=rozkaz.rejestr_B;
		RejestrC=rozkaz.rejestr_C;
		licznik_rozkazow=rozkaz.licznik;	
		adres=rozkaz.couter_rozkaz;
	}
	public void WyslijRejestry(PCB rozkaz)
	{
		rozkaz.rejestr_A=RejestrA;
		rozkaz.rejestr_B=RejestrB;
		rozkaz.rejestr_C=RejestrC;
		rozkaz.licznik=licznik_rozkazow;
		rozkaz.couter_rozkaz=adres;
	}
	//int foo = Integer.parseInt("1234");
	public void WyswietlZawartosc()
	{
		System.out.println("RejestrA: "+this.RejestrA);
		System.out.println("RejestrB: "+this.RejestrB);
		System.out.println("RejestrC: "+this.RejestrC);
		System.out.println("Licznik Rozkazów: "+this.licznik_rozkazow);
	}
	private String Zczytajzpamieci(int licznik, int idprocesu)
	{
		String cos;
		cos= String.valueOf(ram.czytaj_z_pam(licznik,idprocesu));
		return cos;		
	}
	/*private byte[] CharToByte(String str)
	{
		byte [] byteArray = null;
		for (int i=0;i<8;i++)
		{
			byteArray[i]=0;
		}
		char[] charArray = str.toCharArray();
		//Character[] charObjectArray = ArrayUtils.toObject(charArray);
		for (int i=0;i<str.length();i++)
		{
			byteArray[i]=(byte) charArray[i];
		}
		return byteArray;
	}*/
	public void WykonajRozkaz(PCB id)
	{
		PobierzRejestry(id);
		name=adres;
		String stan;
		String wyraz="";
		while(true)
		{
		stan = Zczytajzpamieci(adres,id.id);
		adres=adres+1;
	    if(stan.equals(";")) break;
	    wyraz+=stan;
	  
		}
		System.out.println(wyraz);
		licznik_rozkazow++;
		String rozkaz;
		String parametr = "";
		String parametr1 = "";		
		String[] parametry = wyraz.split(" ");
		rozkaz = parametry[0];
		if(parametry.length > 1) parametr=parametry[1];
		if(parametry.length > 2) parametr1=parametry[2];
		/*
		int b;
		int a=wyraz.indexOf(' ');
		if(wyraz.indexOf(' ',3)!=wyraz.length())
		{
			b=wyraz.indexOf(' ',3);		
			parametr = new String(wyraz.substring(a+1, b));
			parametr1= new String(wyraz.substring(b+1));
		}
		else
		{
			parametr = new String(wyraz.substring(a+1));
			parametr1=null;
		}*/
		switch(rozkaz)
		{
		case "MV":
			if(parametr.equals("A")&&parametr1.equals("B"))RejestrA=RejestrB; else
			if(parametr.equals("A")&&parametr1.equals("C"))RejestrA=RejestrC; else
			if(parametr.equals("B")&&parametr1.equals("A"))RejestrB=RejestrA; else
			if(parametr.equals("B")&&parametr1.equals("C"))RejestrB=RejestrC;else
			if(parametr.equals("C")&&parametr1.equals("A"))RejestrC=RejestrA;else
			if(parametr.equals("C")&&parametr1.equals("B"))RejestrC=RejestrC;else
			if(parametr.equals("A"))RejestrA=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("B"))RejestrB=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("C"))RejestrC=(int) Integer.parseInt(parametr1);		
			
			break;
		case "AD":
			if(parametr.equals("A"))RejestrA+=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("B"))RejestrB+=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("C"))RejestrC+=(int) Integer.parseInt(parametr1);
			break;
		case "AL": 
			if(parametr.equals("B"))RejestrA+=this.RejestrB;else
			if(parametr.equals("C"))RejestrA+=this.RejestrC;
			break;
		case "SB":
			if(parametr.equals("A"))RejestrA-=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("B"))RejestrB-=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("C"))RejestrC-=(int) Integer.parseInt(parametr1);
			break;
		case "SL":
			if(parametr.equals("B"))RejestrA-=this.RejestrB;else
			if(parametr.equals("C"))RejestrA-=this.RejestrC;
			break;
		case "MU":
			if(parametr.equals("A"))RejestrA*=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("B"))RejestrB*=(int) Integer.parseInt(parametr1);else
			if(parametr.equals("C"))RejestrC*=(int) Integer.parseInt(parametr1);
			break;
		case "ML": 
			if(parametr.equals("B"))RejestrA*=RejestrB;else
			if(parametr.equals("C"))RejestrA*=RejestrC;
			break;
		case "IC":
			if(parametr.equals("A"))RejestrA++;else
			if(parametr.equals("B"))RejestrB++;else
			if(parametr.equals("C"))RejestrC++;
			break;
		case "DC":
			if(parametr.equals("A"))RejestrA--;else
			if(parametr.equals("B"))RejestrB--;else
			if(parametr.equals("C"))RejestrC--;
			break;
		case "CF":			
			fat.create_file(parametr);
			break;
		case "WF":
			fat.write_to_file(parametr,parametr1);
			break;
		case "RF": 
			fat.open_file(parametr);
			break;
		case "FC": 			
			fat.close_file();
			break;
		case "DF":
			fat.delete_file(parametr);
			break;
		case "JZ": //JUMP NOT ZEO
			if(parametr.equals("A"))
			{
				if(RejestrA>1)adres=flaga;
			}else
			if(parametr.equals("B"))
			{
				if(RejestrB>1)adres=flaga;
			}else
			if(parametr.equals("C"))
			{
				if(RejestrC>1)adres=flaga;
			}
			break;
		case "ST": 
			flaga=adres;
			break;
		case "MP": 
			//public boolean nowy_proces (String nazwa, int prio)
			procesami.nowy_proces(parametr1,Integer.parseInt(parametr));
			break;
		case "SM":
			System.out.println("Podaj id do ktorego chcesz wyslac wiadomosc:");
			int s = scanner.nextInt();
			IPC.wyslijKomunikat(s,id.id, parametr);
			break;
		case "RM":
			
			if(IPC.odbierzKomunikat(id.id)==null)
			{
					licznik_rozkazow--;
					adres=name;
			}

			break;
		case "FR": 
			//void rename_file(byte[] name,byte[] newname)
			fat.rename_file(parametr,parametr1);
			break;
		case "HT": 
			procesami.usun_proces(id.id);
			break;
		case "CL":
			zamek= new locks();
			kolejka_zamkow.add(zamek);
			break;
		case "LL":
			if(kolejka_zamkow.size()<Integer.parseInt(parametr))
			{
				System.out.println("Nie ma zamka o tym id");
				break;
			}
			else kolejka_zamkow.get(Integer.parseInt(parametr)-1).lock(id.id);
		//	licznik_rozkazow--;
		//	adres=name;
			break;
		case "UL":
			if(kolejka_zamkow.size()<Integer.parseInt(parametr))
			{
				System.out.println("Nie ma zamka o tym id");
				break;
			}
			else kolejka_zamkow.get(Integer.parseInt(parametr)-1).unlock(id.id);
			break;			
		}
	WyslijRejestry(id);
	}
}
