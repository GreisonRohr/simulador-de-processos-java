import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Principal {
	
	public static void main(String[] args) {
		
		//variaveis utilizadas
		int nprocesso=0 ,primeiroCiclo=0,inicializa=0,tempoMedioProcessos=0, tempoEspera=0; 
		int valor_pid=0, contCiclo=0, contProcessoExe=0, totalCiclosExe=0, criaProcesso=0;
		int primeiroCicloCpu=0;
		int a=0,mediaAptos=0,contaApto=0,contExe_Apto=0;
		
		Processo pro = null;
		
		Scanner ler = new Scanner(System.in);

		
		System.out.println("Quantos processos deseja executar:");
	
		nprocesso=ler.nextInt();
		
		//processador
				Processo[] processador = new Processo[1];
				//dispositivos de E/S
				Processo[] noHd = new Processo[1];
				Processo[] noVideo = new Processo[1];
				Processo[] naImpressora = new Processo[1];
				
				//filas 
				ArrayList<Processo> criacao = new ArrayList<Processo>(200);
				ArrayList<Processo> apto = new ArrayList<Processo>(200);
				ArrayList<Processo> encerrados = new ArrayList<Processo>(200);
				ArrayList<Processo> hd = new ArrayList<Processo>(200);
				ArrayList<Processo> video = new ArrayList<Processo>(200);
				ArrayList<Processo> impressora = new ArrayList<Processo>(200);
				
				//lista para armazenar os processos que passaram por cada estado
				
				ArrayList<Processo> contCria = new ArrayList<Processo>(200);
				ArrayList<Processo> contApto = new ArrayList<Processo>(200);
				ArrayList<Processo> contExe = new ArrayList<Processo>(200);
				ArrayList<Processo> contBloq = new ArrayList<Processo>(200);
				ArrayList<Processo> contEncerra = new ArrayList<Processo>(200);


		// validação para usuario digitar maximo de 200 processos
		while(nprocesso>200) {
			System.out.println("Limite Maximo de processos é igual 200!!");
			System.out.println("Quantos processos deseja executar:");
			nprocesso=ler.nextInt();
		}
		
		System.out.println("Tempo de Espera:");
		tempoEspera=ler.nextInt();//recebe o tempo de espera para listar os dados na tela
				
		while(encerrados.size()!=nprocesso) {//esse while representa um ciclo de processador
		
				if(criaProcesso < nprocesso) {//cria os processos com um limite maximo solicitado pelo usuario
					//probabilidade de 10% de criar novo processo
					Random aleatorio = new Random();
					int valor = aleatorio.nextInt(100);
					if(valor<=10) {
						valor_pid++;//valor do pid para cada processo
						criaProcesso++;//conta os processos criados

						pro = new Processo();//cria processo
						
						pro.setpid(valor_pid);//adiciona valor do pid ao processo
						
						int cicloExe = aleatorio.nextInt(150)+50;//sorteia numero de ciclos para executar
						
						pro.setnCiclosExe(cicloExe);//adiciona quantidade de ciclos para executar ao processo

						totalCiclosExe=totalCiclosExe+cicloExe; //contador total de ciclos de todos os processos 
						
						//teste para ver se lista de criaçao possui processos para adicionar a fila de aptos
						if(criacao.size()>0) {
							criacao.get(0).setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto
							apto.add(criacao.get(0));
							contApto.add(criacao.get(0));//contador de processos que passaram pelo estado de apto
							criacao.remove(0);
							
						}
						criacao.add(pro);
						
						contCria.add(pro);//armazena os processo que passaram pelo estado de criação
						
						inicializa=1;//adiciona 1 a variavel indicando que foi criado o primeiro processo
						
						primeiroCiclo = contCiclo;//recebe o ciclo em que foi criado o processo 
							
				}
							
				}
				
				if(inicializa > 0 ) {
					if(contCiclo!=primeiroCiclo) {//faz com que o processo fique um ciclo em estado de criação 
						if(criacao.size()>0) {
							criacao.get(0).setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto
							apto.add(criacao.get(0));//armazena o processo na lista de aptos 
							contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto
							contApto.add(criacao.get(0));//armazena os processos que passaram pelo estado de apto
							criacao.remove(0);
							
							//teste para colocar o primeiro processo criado no processador
							if(processador[0]==null) {
								mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos
								processador[0]=apto.get(0);
								primeiroCicloCpu=contCiclo;//armazena ciclo atual para comparar e garantir que o primeiro ciclo do processo seja de cpu
								contExe.add(apto.get(0));//contador de processos que passaram pelo estado de execução
								apto.remove(0);	
							}	
						}
					}
				}
				
			//teste para controlar o limite maximo de trinta ciclos por processo
				if(contProcessoExe==30) {
					if(processador[0]!=null) {//verifica se o processador possui algum processo
						if(processador[0].getnCiclosExe()==0) {
							encerrados.add(processador[0]);
							contEncerra.add(processador[0]);//armazena os processos que passaram pelo estado de encerrados
							contProcessoExe=0;//zera contador de estado de execução
							if(apto.size()!=0){
								mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

								processador[0]=apto.get(0);
								primeiroCicloCpu=contCiclo;//armazena ciclo atual para comparar e garantir que o primeiro ciclo do processo seja de cpu
								
								//teste para ver se o processo ja foi contado
								a=0;
								for(int i=0; i<contExe.size();i++) {
									if(contExe.get(i)==apto.get(0)) {
										a=1;
									}
									
								}
								if(a==0) {
									contExe.add(apto.get(0));//armazena os processos que passaram pelo estado de execução
								}
								
								apto.remove(0);
								contProcessoExe=0;
							}
							else {
								processador[0]=null;

							}

						}
						else{
							processador[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto
							apto.add(processador[0]);
							contExe_Apto++;//contador de vezes que processos tiveram de ser retirados do estado de execução e devolvidos para a fila de aptos
							contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto

							mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos
							processador[0]=apto.get(0);
							primeiroCicloCpu=contCiclo;//armazena ciclo atual para comparar e garantir que o primeiro ciclo do processo seja de cpu

							
							//teste para ver se o processo ja foi contado
							a=0;
							for(int i=0; i<contExe.size();i++) {
								if(contExe.get(i)==apto.get(0)) {
									a=1;
								}
								
							}
							if(a==0) {
								contExe.add(apto.get(0));//contador de processos que passaram pelo estado de execução
							}
							
							apto.remove(0);
							contProcessoExe=0;

							}
					}	
				}
				else{
					if(processador[0]!=null) {
						if(processador[0].getnCiclosExe()==0) {// testa se um processo ja executou sua quantidade total de ciclos 
							encerrados.add(processador[0]);
							contEncerra.add(processador[0]);//conta os processos que passaram pelo estado de encerrados
							if(apto.size()!=0) {
								mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos
								processador[0]=apto.get(0);
								primeiroCicloCpu=contCiclo;//armazena ciclo atual para comparar e garantir que o primeiro ciclo do processo seja de cpu

								//teste para ver se o processo ja foi contado
								a=0;
								for(int i=0; i<contExe.size();i++) {
									if(contExe.get(i)==apto.get(0)) {
										a=1;
									}
									
								}
								if(a==0) {
									contExe.add(apto.get(0));//contador de processos que passaram pelo estado de execução
								}
								apto.remove(0);
								contProcessoExe=0;
							}
							else {
								processador[0]=null;
								contProcessoExe=0;	
							}

					}
						else {
							contProcessoExe ++;
							processador[0].setnCiclosExe(processador[0].getnCiclosExe() - 1);//decrementa o numero de ciclos de um processo
														
						}
					
					}
				}
				
				
				//criar fila de bloqueados
				if(processador[0]!=null) {
					if(contCiclo>primeiroCicloCpu) {//garante que o primeiro ciclo de processo seja de cpu
						
					Random aleatorio = new Random();
					int valor = aleatorio.nextInt(100);//teste de probabilidade de 1% de solicitar recurso de E/S
					if(valor==1) {
						
						Random sorte = new Random();
						int s = sorte.nextInt(3)+1;//sorteia qual dispositivo solicitou o recurso de E/S
						switch(s) {
						case 1:
							Random nciclos = new Random();
							int hdCiclos = nciclos.nextInt(100)+200;
							processador[0].setnCiclosBloqueado(hdCiclos);
							totalCiclosExe=totalCiclosExe+hdCiclos; //contador total de ciclos de todos os processos 

							if(noHd[0] == null) {
								noHd[0]=processador[0];
								
								//teste para ver se o processo ja foi contado
								a=0;
								for(int i=0; i<contBloq.size();i++) {
									if(contBloq.get(i)==processador[0]) {
										a=1;
									}
									
								}
								if(a==0) {
									contBloq.add(processador[0]);//contador de processos que passaram pelo estado de bloqueado
								}
																
								
								if(apto.size()!=0) {
									mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

									processador[0]=apto.get(0);
									apto.remove(0);
								}
								else {
									processador[0]=null;

								}
								
							}
							else {
								if(noHd[0].getnCiclosBloqueado()==0 ) {
									if(processador[0]==null) {
										processador[0]=noHd[0];
										contProcessoExe=0;
										if(hd.size()!=0) {
											
											noHd[0]=hd.get(0);

											//teste para ver se o processo ja foi contado
											a=0;
											for(int i=0; i<contBloq.size();i++) {
												if(contBloq.get(i)==hd.get(0)) {
													a=1;
												}
												
											}
											if(a==0) {
												contBloq.add(hd.get(0));//contador de processos que passaram pelo estado de bloqueado
											}
											
											hd.remove(0);
											
										}
										else {
											noHd[0]=null;
										}
										
									}
									else {
										noHd[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto
										
										apto.add(noHd[0]);
										contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto

										if(hd.size()!=0) {
											
											noHd[0]=hd.get(0);
											//teste para ver se o processo ja foi contado
											a=0;
											for(int i=0; i<contBloq.size();i++) {
												if(contBloq.get(i)==hd.get(0)) {
													a=1;
												}
												
											}
											if(a==0) {
												contBloq.add(hd.get(0));//contador de processos que passaram pelo estado de bloqueado
											}
											hd.remove(0);
											
										}
										else {
											noHd[0]=null;
										}
										
										
									}
							}
								else {
									hd.add(processador[0]);
									if(apto.size()!=0) {
										mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

										processador[0]=apto.get(0);
										apto.remove(0);
									}
									else {
										processador[0]=null;
									}	
								}

							}
							
							break;
								
						case 2:
							Random nciclosv = new Random();
							int videoCiclos = nciclosv.nextInt(100)+200;
							processador[0].setnCiclosBloqueado(videoCiclos);
							totalCiclosExe=totalCiclosExe+videoCiclos; //contador total de ciclos de todos os processos 

							if(noVideo[0] == null) {
								noVideo[0]=processador[0];
								//teste para ver se o processo ja foi contado
								a=0;
								for(int i=0; i<contBloq.size();i++) {
									if(contBloq.get(i)==processador[0]) {
										a=1;
									}
									
								}
								if(a==0) {
									contBloq.add(processador[0]);//contador de processos que passaram pelo estado de bloqueado
								}
								if(apto.size()!=0) {
									mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

									processador[0]=apto.get(0);
									apto.remove(0);
								}
								else {
									processador[0]=null;

								}
								
							}
							else {
								if(noVideo[0].getnCiclosBloqueado()==0 ) {
									if(processador[0]==null) {
										processador[0]=noVideo[0];
										contProcessoExe=0;
										if(video.size()!=0) {
											
											noVideo[0]=video.get(0);
											
											//teste para ver se o processo ja foi contado
											a=0;
											for(int i=0; i<contBloq.size();i++) {
												if(contBloq.get(i)==video.get(0)) {
													a=1;
												}
												
											}
											if(a==0) {
												contBloq.add(video.get(0));//contador de processos que passaram pelo estado de bloqueado
											}
											
											video.remove(0);
											
										}
										else {
											noVideo[0]=null;
										}
										
									}
									else {
										noVideo[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto

										apto.add(noVideo[0]);
										contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto

										if(video.size()!=0) {
											
											noVideo[0]=video.get(0);
											video.remove(0);
											
										}
										else {
											noVideo[0]=null;
										}
										
										
									}																		
							}
								else {
									video.add(processador[0]);
									if(apto.size()!=0) {
										mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

										processador[0]=apto.get(0);
										apto.remove(0);
									}
									else {
										processador[0]=null;
									}																		
								}

							}
							
							break;

						
						case 3:
							Random nciclosImpre = new Random();
							int impreCiclos = nciclosImpre.nextInt(100)+200;
							processador[0].setnCiclosBloqueado(impreCiclos);
							totalCiclosExe=totalCiclosExe+impreCiclos; //contador total de ciclos de todos os processos 
							if(naImpressora[0] == null) {
								naImpressora[0]=processador[0];
								//teste para ver se o processo ja foi contado
								a=0;
								for(int i=0; i<contBloq.size();i++) {
									if(contBloq.get(i)==processador[0]) {
										a=1;
									}
									
								}
								if(a==0) {
									contBloq.add(processador[0]);//contador de processos que passaram pelo estado de bloqueado
								}
								
								if(apto.size()!=0) {
									mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos
									
									processador[0]=apto.get(0);
									apto.remove(0);
								}
								else {
									processador[0]=null;

								}
								
								
							}
							else {
								if(naImpressora[0].getnCiclosBloqueado()==0 ) {
									if(processador[0]==null) {
										processador[0]=naImpressora[0];
										contProcessoExe=0;
										if(impressora.size()!=0) {
											
											naImpressora[0]=impressora.get(0);
											//teste para ver se o processo ja foi contado
											a=0;
											for(int i=0; i<contBloq.size();i++) {
												if(contBloq.get(i)==impressora.get(0)) {
													a=1;
												}
												
											}
											if(a==0) {
												contBloq.add(impressora.get(0));//contador de processos que passaram pelo estado de bloqueado
											}
											
											impressora.remove(0);
											
										}
										else {
											naImpressora[0]=null;
										}
										
									}
									else {
										naImpressora[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto

										apto.add(naImpressora[0]);
										contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto

										if(impressora.size()!=0) {
											
											naImpressora[0]=impressora.get(0);
											
											impressora.remove(0);
											
										}
										else {
											naImpressora[0]=null;
										}
										
										
									}
									
							}
								else {
									impressora.add(processador[0]);
									
									//teste para ver se o processo ja foi contado
									a=0;
									for(int i=0; i<contBloq.size();i++) {
										if(contBloq.get(i)==processador[0]) {
											a=1;
										}
										
									}
									if(a==0) {
										contBloq.add(processador[0]);//contador de processos que passaram pelo estado de bloqueado
									}
									if(apto.size()!=0) {
										mediaAptos = mediaAptos + (contCiclo - apto.get(0).getmediaEsperaAptos());//calcula tempo que ficou em aptos

										processador[0]=apto.get(0);
										apto.remove(0);
									}
									else {
										processador[0]=null;
									}	
								}

							}
							
							break;
						}
					}
					}
					
				}
				
				//testa o tempo que um processo esta em estado de bloqueado
				if(noHd[0]!=null) {
					
					if(noHd[0].getnCiclosBloqueado()==0 ) {
						if(processador[0]==null) {
							processador[0]=noHd[0];
							contProcessoExe=0;
							if(hd.size()!=0) {
								
								noHd[0]=hd.get(0);
								
								
								hd.remove(0);
								
							}
							else {
								noHd[0]=null;
							}

							
						}
						else {
							noHd[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto

							apto.add(noHd[0]);
							contaApto++;//conta quantidade processos que passaram por apto, para fazer a media de espera de um processo em estado de apto

							if(hd.size()!=0) {
								
								noHd[0]=hd.get(0);
								hd.remove(0);
								
							}
							else {
								noHd[0]=null;
							}
							
						}
				}
					
					
				}
				
				if(noHd[0]!=null) {
					noHd[0].setnCiclosBloqueado(noHd[0].getnCiclosBloqueado()-1);
					

				}
				
				
				if(noVideo[0]!=null) {
					
					if(noVideo[0].getnCiclosBloqueado()==0 ) {
						if(processador[0]==null) {
							processador[0]=noVideo[0];
							contProcessoExe=0;
							if(video.size()!=0) {
								
								noVideo[0]=video.get(0);
								video.remove(0);
								
							}
							else {
								noVideo[0]=null;
							}

							
						}
						else {
							noVideo[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto

							apto.add(noVideo[0]);
							contaApto++;

							if(video.size()!=0) {
								
								noVideo[0]=video.get(0);
								video.remove(0);
								
							}
							else {
								noVideo[0]=null;
							}
							
						}
										
				}
					
					
				}
				
				if(noVideo[0]!=null) {
					noVideo[0].setnCiclosBloqueado(noVideo[0].getnCiclosBloqueado()-1);

				}
				
				
				if(naImpressora[0]!=null) {
					
					if(naImpressora[0].getnCiclosBloqueado()==0 ) {
						if(processador[0]==null) {
							processador[0]=naImpressora[0];
							contProcessoExe=0;
							if(impressora.size()!=0) {
								
								naImpressora[0]=impressora.get(0);
								impressora.remove(0);
								
							}
							else {
								naImpressora[0]=null;
							}

							
						}
						else {
							naImpressora[0].setmediaEsperaAptos(contCiclo);//armazena valor do ciclo que entrou para calcular a media de espera em apto

							apto.add(naImpressora[0]);
							contaApto++;

							if(impressora.size()!=0) {
								
								naImpressora[0]=impressora.get(0);
								impressora.remove(0);
								
							}
							else {
								naImpressora[0]=null;
							}
							
						}					

				}
					
					
				}
				
				if(naImpressora[0]!=null) {
					naImpressora[0].setnCiclosBloqueado(naImpressora[0].getnCiclosBloqueado()-1);

				}
	
				contCiclo++;//contador para contar os ciclos de processador 

				tempoMedioProcessos=totalCiclosExe;//guarda o tempo total de execuçao de todos os processos

					
				//lista os estados dos processos 
				
				System.out.println(contCiclo+" CICLO(S)"+"########################################");//mostra o ciclo atual 
				System.out.println("\n");


				System.out.println("CRIAÇÃO "+" ====================================");

				if(criacao.size()==0) {
					System.out.println(" Nenhum processo criado! ");
					
				}
				else {
					
					for(int i=0; i< criacao.size();i++) {//lista os processos na lista de aptos
						System.out.print("PID:" + criacao.get(i).getpid());
					
						System.out.print("  ");

						System.out.println("Ciclos para Executar:" +criacao.get(i).getnCiclosExe());
					}
					
				}
				
				System.out.println("=============================================");
				
				System.out.println("\n");
				
				
				System.out.println("FILA DE APTOS "+" ==============================");

				if(apto.size()==0) {
					System.out.println("Lista de aptos vazia! ");
					
				}
				else {
					
					for(int i=0; i< apto.size();i++) {//lista os processos na lista de aptos
						System.out.print("PID:" + apto.get(i).getpid());
					
						System.out.print("  ");

						System.out.println("Ciclos para Executar:" +apto.get(i).getnCiclosExe());
					}
					
				}
				
				System.out.println("=============================================");
				
				System.out.println("\n");

				System.out.println("PROCESSO QUE ESTA NO PROCESSADOR "+" ***********");

				
				if(processador[0]==null) {// mostra o proce que esta no processador 
					System.out.println("Processador vazio!");
				}else {
					System.out.print("PID:" + processador[0].getpid());
					
					System.out.print("  ");

					System.out.println("Ciclos para Executar:" +processador[0].getnCiclosExe());
				}
				
				System.out.println("*********************************************");

				
				System.out.println("\n");
				
								
				System.out.println("PROCESSO BLOQUEADO (HD) "+" ____________________");

				
				if(noHd[0]==null) {// mostra o proce que esta usando o hd 
					System.out.println("HD vazio!");
				}else {
					System.out.print("PID:" + noHd[0].getpid());
					System.out.print("  ");
					System.out.println("Ciclos utilizando o HD:" +noHd[0].getnCiclosBloqueado());
				}
				System.out.println("\n");
				
				System.out.println("_____________________________________________");

				
				System.out.println("FILA DE ESPERA DO HD");

				if(hd.size()==0) {
					System.out.println("Lista de espera do hd vazia! ");
					
				}
				else {
					
					for(int i=0; i< hd.size();i++) {//lista os processos na lista do HD
						System.out.println("PID:" + hd.get(i).getpid());
						System.out.println("Ciclos HD:" +hd.get(i).getnCiclosExe());
					}
					
				}
				
				
				System.out.println("_____________________________________________");
				System.out.println("\n");
				
				
				System.out.println("\n");
				
				
				System.out.println("PROCESSO BLOQUEADO (VIDEO) "+" _________________");

				
				if(noVideo[0]==null) {// mostra o processo que esta usando o video 
					System.out.println("Video vazio!");
				}else {
					System.out.print("PID:" + noVideo[0].getpid());
					System.out.print("  ");
					System.out.println("Ciclos utilizando o VIDEO:" +noVideo[0].getnCiclosBloqueado());
				}
				System.out.println("\n");
				
				System.out.println("_____________________________________________");

				
				System.out.println("FILA DE ESPERA DO VIDEO");

				if(video.size()==0) {
					System.out.println("Lista de espera do video vazia! ");
					
				}
				else {
					
					for(int i=0; i< video.size();i++) {//lista os processos na lista de videos
						System.out.println("PID:" + video.get(i).getpid());
						System.out.println("Ciclos Bloqueado:" +video.get(i).getnCiclosExe());
					}
					
				}
				
				
				System.out.println("_____________________________________________");
				System.out.println("\n");
				
				
				System.out.println("\n");
				
				
				System.out.println("PROCESSO BLOQUEADO (IMPRESSORA) "+" ____________");

				
				if(naImpressora[0]==null) {// mostra o proce que esta usando o hd 
					System.out.println("IMPRESSORA livre!");
				}else {
					System.out.print("PID:" + naImpressora[0].getpid());
					System.out.print("  ");
					System.out.println("Ciclos utilizando IMPRESSORA:" +naImpressora[0].getnCiclosBloqueado());
				}
				System.out.println("\n");
				
				System.out.println("_____________________________________________");

				
				System.out.println("FILA DE ESPERA DA IMPRESSORA");

				if(impressora.size()==0) {
					System.out.println("Lista de espera da impressora vazia! ");
					
				}
				else {
					
					for(int i=0; i< impressora.size();i++) {//lista os processos na lista de espera da impressora
						System.out.println("apto PID:" + impressora.get(i).getpid());
						System.out.println("Ciclos utilizando a IMPRESSORA:" +impressora.get(i).getnCiclosExe());
					}
					
				}
				
				
				System.out.println("_____________________________________________");
				System.out.println("\n");
				
				
				System.out.println("FILA DE DESTRUIDOS "+" xxxxxxxxxxxxxxxxxxxxxxxxx");

				if(encerrados.size()==0) {
					System.out.println("Lista de Destruidos vazia! ");
					
				}
				else {
					
					for(int i=0; i< encerrados.size();i++) {//lista os processos na lista de destruidos
						System.out.println("PID:" + encerrados.get(i).getpid());
						System.out.println("Quantidade de ciclos para executar:" +encerrados.get(i).getnCiclosExe());
					}
					
				}
				
				System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
				

				
				System.out.println("\n"+"\n");
					
				try{
				      Thread.sleep(tempoEspera*1000);
				}catch(Exception e){
				      System.out.println("Deu erro!");
				}	
				
			

		}// fecha while principal
		
			System.out.println("RELATORIO:");
			System.out.println("\n");

			System.out.println("Total de Ciclos:" + contCiclo);
			System.out.println("\n");
			
			System.out.println("Total de Processos:" + criaProcesso);
			System.out.println("\n");
			
			System.out.println("Tempo total médio (número de ciclos) de execução de um processo:" + tempoMedioProcessos/criaProcesso);
			System.out.println("\n");
			
			System.out.println("Total de Processos que passaram pelo estado de criação:"+contCria.size());
			if(contCria.size()==0) {
				System.out.println(" Nenhum processo passou pelo estado de criação! ");
				
			}
			else {

				for(int i=0; i< contCria.size();i++) {//lista os processos que passaram pelo estado de criação
					System.out.println("PID:" + contCria.get(i).getpid());
				
				}
				
			}
			System.out.println("\n");
			
			System.out.println("Total de Processos que passaram pelo estado de Apto:" + contApto.size());
			if(contApto.size()==0) {
				System.out.println(" Nenhum processo passou pelo estado de Apto! ");
				
			}
			else {

				for(int i=0; i< contApto.size();i++) {//lista os processos que passaram pelo estado de apto
					System.out.println("PID:" + contApto.get(i).getpid());
				
				}
				
			}
			System.out.println("\n");
			
			System.out.println("Total de Processos que passaram pelo estado de Execução:" + contExe.size());
			if(contExe.size()==0) {
				System.out.println(" Nenhum processo passou pelo estado de Apto! ");
				
			}
			else {

				for(int i=0; i< contExe.size();i++) {//lista os processos que passaram pelo estado de execução
					System.out.println("PID:" + contExe.get(i).getpid());
				
				}
				
			}
			System.out.println("\n");

			System.out.println("Total de Processos que passaram pelo estado de Bloqueado:"+contBloq.size() );
			if(contBloq.size()==0) {
				System.out.println(" Nenhum processo passou pelo estado de Bloqueado! ");
				
			}
			else {

				for(int i=0; i< contBloq.size();i++) {//lista os processos que passaram pelo estado de Bloqueado
					System.out.println("PID:" + contBloq.get(i).getpid());
				
				}
				
			}	
			System.out.println("\n");

			System.out.println("Total de Processos que passaram pelo estado de Destruido:"+contEncerra.size() );
			if(contEncerra.size()==0) {
				System.out.println(" Nenhum processo passou pelo estado de Encerrados! ");
				
			}
			else {

				for(int i=0; i< contEncerra.size();i++) {//lista os processos que passaram pelo estado de destruido
					System.out.println("PID:" + contEncerra.get(i).getpid());
				
				}
				
			}	
			System.out.println("\n");
			
			System.out.println("Tempo médio de espera (em ciclos) de um processo na fila de aptos:" + mediaAptos/contaApto);
			System.out.println("\n");

			System.out.println("Quantidade de vezes que processos tiveram de ser retirados do estado de execução e devolvidos para a fila de aptos:"+contExe_Apto);			
		
	}
	
	

}
