package OS;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;
		

public class Shell {
	
	private locks lock;
	private FAT8 fat;
	private procesor procesor;
	private Zarzadzanie_procesami zarzadca;
	private PCB pcb;
	private zarzadzanie_pam ram;
	private Interpreter1 interpreter;
	
public void SIEMANO_KOLANO(){
		
		System.out.println("BBBBBBBBBBBBBBBBB             JJJJJJJJJJJ     OOOOOOOOO        SSSSSSSSSSSSSSS");
		System.out.println("B::::::::::::::::B            J:::::::::J   OO:::::::::OO    SS:::::::::::::::S");
		System.out.println("B::::::BBBBBB:::::B           J:::::::::J OO:::::::::::::OO S:::::SSSSSS::::::S");
		System.out.println("BB:::::B     B:::::B          JJ:::::::JJO:::::::OOO:::::::OS:::::S     SSSSSSS");
		System.out.println("  B::::B     B:::::B            J:::::J  O::::::O   O::::::OS:::::S            ");
		System.out.println("  B::::B     B:::::B            J:::::J  O:::::O     O:::::OS:::::S            ");
		System.out.println("  B::::BBBBBB:::::B             J:::::J  O:::::O     O:::::O S::::SSSS         ");
		System.out.println("  B:::::::::::::BB              J:::::j  O:::::O     O:::::O  SS::::::SSSSS    ");
		System.out.println("  B::::BBBBBB:::::B             J:::::J  O:::::O     O:::::O    SSS::::::::SS  ");
		System.out.println("  B::::B     B:::::BJJJJJJJ     J:::::J  O:::::O     O:::::O       SSSSSS::::S ");
		System.out.println("  B::::B     B:::::BJ:::::J     J:::::J  O:::::O     O:::::O            S:::::S");
		System.out.println("  B::::B     B:::::BJ::::::J   J::::::J  O::::::O   O::::::O            S:::::S");
		System.out.println("BB:::::BBBBBB::::::BJ:::::::JJJ:::::::J  O:::::::OOO:::::::OSSSSSSS     S:::::S");
		System.out.println("B:::::::::::::::::B  JJ:::::::::::::JJ    OO:::::::::::::OO S::::::SSSSSS:::::S");
		System.out.println("B::::::::::::::::B     JJ:::::::::JJ        OO:::::::::OO   S:::::::::::::::SS ");
		System.out.println("BBBBBBBBBBBBBBBBB        JJJJJJJJJ            OOOOOOOOO      SSSSSSSSSSSSSSS   ");
		System.out.println("");
		
}
	
	public void help(){
		
		System.out.println("-----Dostepne Polecenia-----");
		System.out.println("load    - lÂaduje program");
		System.out.println("execall - wykonuje wszystkie zaladowane programy");
		System.out.println("step    - wykonuje 1 krok");
		System.out.println("dproc   - wyswietla procesy");
		System.out.println("pcbc    - wyswietla zawartosc wybranego pcb");
		System.out.println("dready  - wyswietla procesy w stanie 'ready' ");
		System.out.println("sque    - wyswietla kolejke semafora danego procesu");
		System.out.println("lockqueue    - wyswietla kolejke zamkow");
		System.out.println("dcom    - wyswietla kolejke komunikatow danego procesu");
		System.out.println("slawek  - wyswietla rejestry pomocnice interpretera");
		System.out.println("mem - wypisuje zawartosc pamieci operacyjnej");
		System.out.println("memempty - wypisuje liste wolnych stron pamieci operacyjnej");
		System.out.println("memused - wypisuje liste zajetych stron pamieci operacyjnej");
		System.out.println("virtmem - wypisuje zawartosc pamieci wirtualnej");
		System.out.println("virtempty - wypisuje liste wolnych stron pamieci wirtualnej");
		System.out.println("virtused - wypisuje liste zajetych stron pamieci wirtualnej");
		System.out.println("createf	- tworzy nowy plik");
		System.out.println("deletef	- usuwa plik");
		System.out.println("root	- wypisywanie roota");
		System.out.println("data	- wypisywanie sektorow danych");
		System.out.println("file	- wyswietla zawartosc pliku");
		System.out.println("fattab  - wyswietla zawartosc FAT");
		System.out.println("dcat    - wyswietla zawartosc katalogu glownego");
		System.out.println("quit    - wyjscie z systemu");
		System.out.println("------------------------------");
		
	}
	
	public Shell(FAT8 fat1,procesor PROC,Zarzadzanie_procesami id, zarzadzanie_pam ram1, Interpreter1 interpreter1)
	{
		zarzadca=id;
		procesor=PROC;
		fat= fat1;
		ram = ram1;
		interpreter = interpreter1;
		
	}

	
	
	public void installPrograms(){
		
		fat.create_file("p1");
		fat.write_to_file("p1", "MV A 4;MV B A;ST;DC B;ML B;JZ B;HT;");
		fat.close_file();
		fat.create_file("p2");
		fat.write_to_file("p2", "CF plik;WF plik mariusz;FC;FR plik program;DF program;HT;");
		fat.close_file();
		fat.create_file("p3");
		fat.write_to_file("p3", "RM;RM;MV A 3;HT;"); // Tworze p3, potem p4 z wyzszym priorytetem
		fat.close_file();
		fat.create_file("p4");
		fat.write_to_file("p4", "SM WiadomoscODP4;HT;");
		fat.close_file();
		fat.create_file("p5");
		fat.write_to_file("p5", "CL;LL 1;MV A 3;UL 1;HT;"); // PROCES TWORZONY JAKO DRUGI Tworzenie zamka -> Lock ID=1
		fat.close_file();
		fat.create_file("p6");
		fat.write_to_file("p6", "LL 1;UL 1;HT;"); //PROCES TWORZONY JAKO TRZECI  WywoÃÂywanie zamka 1 , PrÃÂ³ba Unlocku
		fat.close_file();
		fat.create_file("p7");
		fat.write_to_file("p7", "CL;LL 4;MV A 3;UL 4;HT;"); // PROCES TWORZONY JAKO DRUGI Tworzenie zamka -> Lock ID=1
		fat.close_file();
	}
	
	public void shellStart() {
		
		
		
		
		Scanner scanner = new Scanner(System.in);
		
	
		String polecenie = " ";
		while(!polecenie.equals("quit")){
			
			System.out.print("Podaj polecenie:");
			polecenie = scanner.nextLine();											//zaladuj program
			
			if(polecenie.equals("load")){
				System.out.println("Podaj nazwe programu do wykonania:");
			try{
				String nazwa = scanner.nextLine();
				if(fat.open_file(nazwa) != null){
				System.out.println("Podaj priorytet procesu:");
				int s = scanner.nextInt();
				int id = zarzadca.nowy_proces(nazwa, s);
				ram.wirtualna.wpisz_do_pamieci_wirtualnej(id, nazwa);		
					}
				else
					System.out.println("Nie ma takiego pliku");
				}
			catch (InputMismatchException e) {
		            System.out.println("Podano zle dane");
		        }
				continue;
					
			}
			
			
			else if(polecenie.equals("step")){							//exec step
				
				try {
					procesor.Dzialaj(interpreter);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println("");
				
				
				
				continue;
			}
			
			else if(polecenie.equals("execall")){       //exec all
			    
				   while(zarzadca.lista_PCB.size() > 0){
				    try {
				     procesor.Dzialaj(interpreter);
				    } catch (InterruptedException e) {
				     e.printStackTrace();
				    }
				    System.out.println("");
				   }
			}
				   
			else if(polecenie.equals("dcat")){ 			// pliki	display catalog
				//
											
				System.out.println("display catalog");					
				fat.print_ROOT(true);
				
				continue;
			}
			else if(polecenie.equals("createf")){ 				//createfile
			
			try{
				System.out.println("Podaj nazwe nowego pliku:");
				String nazwa = scanner.nextLine();
				fat.create_file(nazwa);		
				continue;
			}
			catch (InputMismatchException e) {
	            System.out.println("Podano zle dane");
	        }
			}
			else if(polecenie.equals("deletef")){				//delete file
				
			try{
				System.out.println("Podaj nazwe pliku do usuniecia:");
				String nazwa = scanner.nextLine();
				fat.delete_file(nazwa);
				System.out.println("usunieto plik: " + nazwa);
			}
			catch (InputMismatchException e) {
	            System.out.println("Podano zle dane");
	        }
				
				continue;
			}
				
				
			else if(polecenie.equals("fattab")){							//display fat tab
			    fat.print_FAT(false);
			     continue;
			    } 
			
			else if(polecenie.equals("root")){									//display root
			    fat.print_ROOT(false);
			    fat.print_ROOT(true);
			     continue;
			} 
			
			else if(polecenie.equals("data")){										//display data blocks
			    
			    fat.print_DATA(true);
			 continue;
			}  
			
			   
			else if(polecenie.equals("file")){										//display file
			    
			    
				try{
					System.out.println("Podaj nazwe pliku do wyswietlenia:");
					String nazwa = scanner.nextLine();
					if(fat.open_file(nazwa) != null){
					fat.print_File_ROOT(nazwa, false);
				    fat.print_File_DATA(nazwa, true);
					
					}
					else
					System.out.println("Nie ma takiego pliku");
				}
				catch (InputMismatchException e) {
		            System.out.println("Podano zle dane");
		        }
			    
			    
			    
			     continue;
			}																	
			
			
			else if(polecenie.equals("dproc")){						//display processes			
				
				System.out.println("display processes");				
				zarzadca.lista_procesow();
				continue;
			}
			else if(polecenie.equals("pcbc")){						//pcb contents
				System.out.println("pcb contents");
				System.out.println("Podaj id procesu:");
			
			try{
				int s = scanner.nextInt();
				zarzadca.wypisz_proces(s);
				
			}
			catch (InputMismatchException e) {
	            System.out.println("Podano zle dane:");
	        }
				continue;
			}															
																		//procesor
			
			
			else if(polecenie.equals("dready")){
			//	System.out.println("display ready");					//display ready
				procesor.WypiszGotowe();
				continue;
			}															
			
																		//semafory
			
			
			
			else if(polecenie.equals("sque")){						//semapahore queue
				System.out.println("Podaj id procesu:");
			try{
				int s = scanner.nextInt();
				if(zarzadca.lista_PCB.size()>=1)
				{
				pcb = zarzadca.znajdz_proces(s);
				if(pcb!=null)
				{
				pcb.semafor.show_semaphore_queue();
				}
				else
				{
					System.out.println("Nie ma procesu o tym id");
				}
				}
				else
				{
					System.out.println("Nie ma procesow");
				}
				
			}
			catch (InputMismatchException e) {
	            System.out.println("Podano zle dane");
	        }
			
				continue;
			}
			
																		//komunikacja
			
			
			else if(polecenie.equals("dcom")){							//display communication
				System.out.println("Podaj id procesu:");
				try{
				int s = scanner.nextInt();
				if(zarzadca.lista_PCB.size()>=1)
				{
				IPC.wyswietlKolejke(s);
				}
				else
				{
					System.out.println("Nie ma procesow");
				}
				}
				catch (InputMismatchException e) {
		            System.out.println("Podano zle dane");
		        }
				continue;
			}
			else if(polecenie.equals("slawek")){						//display interpreter registers
				System.out.println("");
				interpreter.WyswietlZawartosc();
				continue;
			}
			else if(polecenie.equals("help")){						//help lul
				help();
				
				continue;
			}
			
			else if(polecenie.equals("mem")){						//displays memory contents
				System.out.println("");
				ram.print_memory();
				continue;
			}
			else if(polecenie.equals("memempty")){						//displays empty memory pages
				System.out.println("");
				ram.wyswietl_liste_wolnych_ramek();
				continue;
			}
			else if(polecenie.equals("memused")){					//dipslays used memory pages
				System.out.println("");
				ram.wyswietl_liste_zajetych_ramek();
				continue;
			}
			else if(polecenie.equals("virtmem")){					//displays virtual memory
				System.out.println("");
				ram.wirtualna.wypisz_pamiec();;
				continue;
			}
			else if(polecenie.equals("virtempty")){					//displays empty virtual memory pages
				System.out.println("");
				ram.wirtualna.wyswietl_liste_wolnych_ramek();;
				continue;
			}
			else if(polecenie.equals("virtused")){						//displays used virtual memory pages
				System.out.println("");
				ram.wirtualna.wyswietl_liste_zajetych_ramek();
				continue;
			}
			
			else if(polecenie.equals("lockqueue")){						//displays used virtual memory pages
				System.out.println("");
				for(int mn=0;mn<interpreter.kolejka_zamkow.size();mn++)
				{
					interpreter.kolejka_zamkow.get(mn).show_lock_queqe();
				}
				
				continue;
			}
		
	
		}
	}
	
}



