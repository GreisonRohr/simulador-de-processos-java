
public class Processo {
	
	int pid,nCiclosExe,nCiclosBloqueado,mediaEsperaAptos,contPerdeProcessador;
	
	public int getpid(){
		return pid;
	}
	
	public void setpid(int pid) {
		this.pid = pid;
	}
	
	public int getmediaEsperaAptos(){
		return mediaEsperaAptos;
	}
	
	public void setmediaEsperaAptos(int mediaEsperaAptos) {
		this.mediaEsperaAptos = mediaEsperaAptos;
	}
	
	public int getnCiclosExe(){
		return nCiclosExe;
	}
	
	public void setnCiclosExe(int nCiclosExe) {
		this.nCiclosExe = nCiclosExe;
	}
	
	public int getnCiclosBloqueado(){
		return nCiclosBloqueado;
	}
	
	public void setnCiclosBloqueado(int nCiclosBloqueado) {
		this.nCiclosBloqueado = nCiclosBloqueado;
	}

}
