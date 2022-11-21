package com.digital.opuserp.view.modulos.acesso.contratos_acesso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.joda.time.DateTime;
import org.joda.time.Months;

import com.digital.opuserp.dao.ContasReceberDAO;
import com.digital.opuserp.dao.CredenciaisAcessoDAO;
import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.domain.Concentrador;
import com.digital.opuserp.domain.RadAcct;
import com.digital.opuserp.domain.RadUserGroupDAO;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.HuaweiUtil;
import com.digital.opuserp.util.MikrotikUtil;
import com.digital.opuserp.util.Real;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

public class VisualizarContratoInfoTecnica extends Window {

	//private Item item;
	Button btCancelar;
	Button btAtualizar;
	Button btDesconectar;
	Button btTempoReal;
	VerticalLayout vlRoot;
	//FieldGroup fieldGroup;
	Integer codAcesso;
	AcessoCliente ac;
	Concentrador base; 
	TabSheet tbRoot;
	VerticalLayout vlconexao;
	VerticalLayout vlRootConexao;
	VerticalLayout vlRootConsumo;
	Map<String, String> regTable;
	Map<String, String> activeConnections;
	RadAcct radacct;
	Map<String, String> queue;
	String bc = "0,00";
	String bi = "0,00";
	
	boolean logado;
	boolean statusConcentrador;
	boolean comunicacao;
	
	EntityManager em = ConnUtil.getEntity();
	
	String[] info = null;
	String vlan;
	public VisualizarContratoInfoTecnica(Integer cod_acesso,String vlan, String title, boolean modal){
			
			if(vlan != null){
				this.vlan = vlan;
			}
		
			codAcesso = cod_acesso;
			ac = em.find(AcessoCliente.class,codAcesso);
			base = ac.getBase();			
			
		
		
		setWidth("950px");
		setHeight("729px");
		
		
		setCaption(title);
		setModal(modal);
		setResizable(true);
		setClosable(false);
		center();
		
		vlRoot = new VerticalLayout();	
		vlRoot.setWidth("100%");
		vlRoot.setMargin(true);
//		vlRoot.setStyleName("border-form");
		
		setContent(new VerticalLayout(){
			{
				setWidth("100%");
				setMargin(true);
				addComponent(vlRoot);
				
				HorizontalLayout hlButtons = new HorizontalLayout();
				hlButtons.setStyleName("hl_buttons_bottom");
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.addComponent(buildBtAtualizar());
				hlButtons.addComponent(buildBtCancelar());
				btCancelar.focus();
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
			}

		});
		
		buildLayout();
	}
	
	String url_chart = "";
	String url_consumo = "";
	public VerticalLayout buildVlConsumo(){
			
		
		vlRootConsumo = new VerticalLayout(){
			{
				try {
					
					url_consumo = "http://172.17.0.71/consumo/?username="+codAcesso.toString();
					
			        
			        final Embedded consumo = new Embedded(null,new ExternalResource(url_consumo));
			        consumo.setType(Embedded.TYPE_BROWSER);
			        consumo.setWidth("100%");
			        consumo.setHeight("506px");
			        //consumo.setStyleName("center-margin");
	
			        
			        Button btRefresh = new Button("Atualizar", new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							consumo.setSource(null); 
							consumo.setSource(new ExternalResource(url_consumo));
							
						}
					});
			        btRefresh.setStyleName(Reindeer.BUTTON_LINK);
			        btRefresh.addStyleName("btRealTime");
			        
			        

			        addComponent(consumo);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		};
		
		vlRootConsumo.setSizeFull();
		
		return vlRootConsumo;
	}
	
	public VerticalLayout buildVlConecao(){
		
		CredenciaisAcessoDAO caDAO = new CredenciaisAcessoDAO();
		radacct = caDAO.buscarRadacct(ac.getLogin());
		
		vlconexao = new VerticalLayout(){
			{
				
				if(base!=null && base.getTipo().equals("mikrotik")){
					queue = MikrotikUtil.listarQueuesList(base.getUsuario(),base.getSenha(),base.getEndereco_ip(),Integer.parseInt(base.getPorta_api()),ac.getLogin());
				}
				
				if(base != null && base.getTipo().equals("huawei")){
					info = HuaweiUtil.pegarInformacoes(ac.getLogin());
				}
				
				setMargin(true);
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfStatusConcent= new TextField("Status Concentrador");				
						tfStatusConcent.setWidth("150px");	
						
						if(base!=null){						
												
							if(base.getTipo().equals("mikrotik")){
								statusConcentrador = MikrotikUtil.testconexao(base.getUsuario(),base.getSenha(),base.getEndereco_ip(),Integer.parseInt(base.getPorta_api()));
							}
							if(base.getTipo().equals("huawei") && info != null){
								statusConcentrador = true;
							}
							
						}
	
						if(statusConcentrador){						
							tfStatusConcent.setValue("UP");	
						}else{
							tfStatusConcent.setValue("DOWN");	
						}
						tfStatusConcent.setReadOnly(true);
						tfStatusConcent.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfStatusConcent);
						setExpandRatio(tfStatusConcent, 2);		
						
						if(tfStatusConcent.getValue().equals("UP")){
							tfStatusConcent.addStyleName("valid-cpf");
						}else{
							tfStatusConcent.addStyleName("invalid-cpf");
						}
					}
				});
								
						addComponent(new FormLayout(){					
							{
								setMargin(true);
								setSpacing(true);
								setStyleName("form-cutom-new");		
								
								TextField tfComunConcen= new TextField("Comunicação Concentrador");				
								tfComunConcen.setWidth("150px");			

								if(base!=null){
									
									if(base.getTipo().equals("mikrotik")){
									
											comunicacao =	MikrotikUtil.buscarComunicacao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getEndereco_mac());
											logado = MikrotikUtil.buscarStatusConexao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getLogin());
									}
									
									if(base.getTipo().equals("huawei")){
											
											
											if(info != null){
												comunicacao = true;
												logado = true;
											}
									}
								}
	
								if(comunicacao || logado){
									tfComunConcen.setReadOnly(false);						
									tfComunConcen.setValue("ESTABELECIDA");
									tfComunConcen.setReadOnly(true);										
								}else{
									tfComunConcen.setReadOnly(false);						
									tfComunConcen.setValue("NÃO ESTABELECIDA");						
									tfComunConcen.setReadOnly(true);												
								}
								
								tfComunConcen.setReadOnly(true);
								tfComunConcen.setStyleName("caption-align-visualizar-conexao");
								
								addComponent(tfComunConcen);
								setExpandRatio(tfComunConcen, 2);	
								
								if(tfComunConcen.getValue().equals("ESTABELECIDA")){
									tfComunConcen.addStyleName("valid-cpf");
								}else{
									tfComunConcen.addStyleName("invalid-cpf");
								}
							}
						});
						
						

				addComponent(new HorizontalLayout(){
					{
					addComponent(new FormLayout(){					
						{
							setMargin(true);
							setSpacing(true);
							setStyleName("form-cutom");		
							
							TextField tfStatusConexão= new TextField("Status de Conexão");				
							tfStatusConexão.setWidth("150px");			

							if(logado){
								tfStatusConexão.setReadOnly(false);						
								tfStatusConexão.setValue("LOGADO");
								tfStatusConexão.setReadOnly(true);										
							}else{
								tfStatusConexão.setReadOnly(false);						
								tfStatusConexão.setValue("NÃO LOGADO");						
								tfStatusConexão.setReadOnly(true);												
							}
							
							tfStatusConexão.setReadOnly(true);
							tfStatusConexão.setStyleName("caption-align-visualizar-conexao");
							
							addComponent(tfStatusConexão);
							setExpandRatio(tfStatusConexão, 2);	
							
							if(tfStatusConexão.getValue().equals("LOGADO")){
								tfStatusConexão.addStyleName("valid-cpf");
							}else{
								tfStatusConexão.addStyleName("invalid-cpf");
							}
						}
					});

					
					btDesconectar = new Button("Desconectar", new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							boolean check = false;
							if(base!=null){
								
								if(base.getTipo().equals("mikrotik")){
									check = MikrotikUtil.desconectarCliente(base.getUsuario(), base.getSenha(), base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()),ac.getLogin());
								}
								
								if(base.getTipo().equals("huawei")){
									check = HuaweiUtil.desconectarCliente(ac.getLogin());
								}
							}
							
							
							if(check){
								Notification.show("O Cliente foi Desconectado Com Sucesso!");
							}else{
								Notification.show("O Cliente já esta Desconectado!");
							}
							
							vlRootConexao.replaceComponent(vlconexao, buildVlConecao());
							
						}
					});	
					
					btDesconectar.setStyleName("atualizar");
					
					
					
					btTempoReal = new Button("Tempo Real", new Button.ClickListener() {
						
						@Override
						public void buttonClick(ClickEvent event) {
							
							String username = ac.getLogin();
							String url_consumo = "http://172.17.0.71/consumo2/?u="+username;
							
							Window w = new Window();
							w.setWidth("781px");
							w.setHeight("561px");
							w.setCaption("Tráfego em tempo real ( "+username+" )");
							w.center();
							w.setResizable(false);							
													
							final Embedded consumo = new Embedded(null,new ExternalResource(url_consumo));
					        consumo.setType(Embedded.TYPE_BROWSER);
					        consumo.setSizeFull();
					        
					        w.setContent(consumo);
							
							getUI().addWindow(w);	
						}
					});	
					
					btTempoReal.setStyleName("atualizar");
					
					
					FormLayout flmBt = new FormLayout(){
						{					
							setMargin(true);
							setSpacing(true);	
							setStyleName("form-cutom");
							addComponent(btDesconectar);
							addComponent(btTempoReal);		
						}
					};
					
					addComponent(flmBt);
			
				}
			});

				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfRadioName= new TextField("Radio Name");				
						tfRadioName.setWidth("150px");			
		
						if(base!=null && base.getTipo().equals("mikrotik")){
							regTable = MikrotikUtil.buscarRegistationTable(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getEndereco_mac());
							activeConnections  = MikrotikUtil.buscarActiveConnections(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getEndereco_mac());
						}
						
						if(regTable!=null && regTable.get("radio-name") !=null){
							
							tfRadioName.setValue(regTable.get("radio-name"));												
						}
						tfRadioName.setReadOnly(true);
						tfRadioName.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfRadioName);
						setExpandRatio(tfRadioName, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfModoConexão= new TextField("Modo de Conexão");				
						tfModoConexão.setWidth("150px");	
						
						if(radacct!=null){
							if(radacct.getFramedprotocol().equals("PPP")){
								tfModoConexão.setValue("PPPOE");				
							}else{
								tfModoConexão.setValue("HOTSPOT");
							}

						}else{
							tfModoConexão.setValue("");																										
						}
						
						tfModoConexão.setReadOnly(true);
						tfModoConexão.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfModoConexão);
						setExpandRatio(tfModoConexão, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfUptime= new TextField("Uptime");				
						tfUptime.setWidth("150px");			

						if(base.getTipo().equals("mikrotik")){
							if(activeConnections!=null && activeConnections.get("uptime") !=null){	
								tfUptime.setValue(activeConnections.get("uptime"));												
							}
						}
						
						if(base.getTipo().equals("huawei") && info != null && info.length > 0){
							tfUptime.setValue(info[0]);
						}
						
						
						tfUptime.setReadOnly(true);
						tfUptime.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfUptime);
						setExpandRatio(tfUptime, 2);						
					}
				});
				
				addComponent(new HorizontalLayout(){
				{
					addComponent(new FormLayout(){					
						{
							setMargin(true);
							setSpacing(true);
							setStyleName("form-cutom");		
							
							TextField tfEndIpPool= new TextField("Endereço Ip Adress List");				
							tfEndIpPool.setWidth("200px");	
						
							if(base.getTipo().equals("mikrotik")){								
									if(logado){		 							
										String ip = MikrotikUtil.getRemoteIpPPOE(ac.getBase().getEndereco_ip(), ac.getBase().getUsuario(), ac.getBase().getSenha(), ac.getLogin());
										tfEndIpPool.setValue(ip);									
									}			
							}
							
							if(base.getTipo().equals("huawei") && info != null && info.length > 1){								
								tfEndIpPool.setValue(info[2]);
							}
														
							tfEndIpPool.setReadOnly(true);
							tfEndIpPool.setStyleName("caption-align-visualizar-conexao");
							
							addComponent(tfEndIpPool);
							setExpandRatio(tfEndIpPool, 2);		
						}
					});
				
					addComponent(new FormLayout(){					
						{
							setMargin(true);
							setSpacing(true);
							setStyleName("form-cutom-new");		
							
							TextField tfQueuesUp= new TextField("Queues Upload");				
							tfQueuesUp.setWidth("80px");			
							
							if(base.getTipo().equals("mikrotik")){								
								if(queue!=null && queue.get("max-limit")!=null){		
									String queueUp [] = queue.get("max-limit").split("/");
									tfQueuesUp.setValue(queueUp[0]);												
								}									
							}
							
							if(base.getTipo().equals("huawei") && info != null && info.length > 2){								
								tfQueuesUp.setValue(info[3]);
							}
							
							String bloqueado = RadUserGroupDAO.getPlanoByUsername(ac.getLogin());
							
							tfQueuesUp.setReadOnly(true);
							tfQueuesUp.setStyleName("caption-align-visualizar-UpDown");
							
							if(!bloqueado.equals("BLOQUEADO")){							
								tfQueuesUp.addStyleName("valid-cpf");
							}else{
								tfQueuesUp.addStyleName("invalid-cpf");
							}
							
							addComponent(tfQueuesUp);
							setExpandRatio(tfQueuesUp, 2);						
						}
					});
				}
			});
				
				addComponent(new HorizontalLayout(){
					{
						addComponent(new FormLayout(){					
							{
								setMargin(true);
								setSpacing(true);
								setStyleName("form-cutom-new");		
								
								TextField tfEndIpAdList= new TextField("Endereço Fixo");				
								tfEndIpAdList.setWidth("200px");			
								
								if(ac.getEndereco_ip()!=null){
									tfEndIpAdList.setValue(ac.getEndereco_ip());											
								}
								tfEndIpAdList.setReadOnly(true);
								tfEndIpAdList.setStyleName("caption-align-visualizar-conexao");
								
								addComponent(tfEndIpAdList);
								setExpandRatio(tfEndIpAdList, 2);										
							}
						});
						
						addComponent(new FormLayout(){					
							{
								setMargin(true);
								setSpacing(true);
								setStyleName("form-cutom");		
								
								
								TextField tfQueuesDown= new TextField("Queues Download");				
								tfQueuesDown.setWidth("80px");	
								
								if(base.getTipo().equals("mikrotik")){									
										if(queue!=null && queue.get("max-limit")!=null){		
											String queueDown [] = queue.get("max-limit").split("/");
											tfQueuesDown.setValue(queueDown[1]);												
										}							
								}
								
								if(base.getTipo().equals("huawei") && info != null && info.length > 3){								
									tfQueuesDown.setValue(info[4]);
								}
								
								
								String bloqueado = RadUserGroupDAO.getPlanoByUsername(ac.getLogin());
		
								tfQueuesDown.setReadOnly(true);
								tfQueuesDown.setStyleName("caption-align-visualizar-UpDown");
								
								if(!bloqueado.equals("BLOQUEADO")){
									tfQueuesDown.addStyleName("valid-cpf");
								}else{
									tfQueuesDown.addStyleName("invalid-cpf");
								}
								
								addComponent(tfQueuesDown);
								setExpandRatio(tfQueuesDown, 2);						
							}
						});
					}
				});
				
				addComponent(new HorizontalLayout(){
					{
						addComponent(new FormLayout(){					
							{
								setMargin(true);
								setSpacing(true);
								setStyleName("form-cutom-new");		
								
								TextField tfMacAdress= new TextField("MAC Adress");				
								tfMacAdress.setWidth("200px");			

								if(base.getTipo().equals("mikrotik")){
									
									if(regTable!=null && regTable.get("mac-address") !=null){	
										tfMacAdress.setValue(regTable.get("mac-address"));												
									}																	
								}
								
								if(base.getTipo().equals("huawei") && info != null && info.length > 0){								
									tfMacAdress.setValue(info[1]);
								}
								
								
								tfMacAdress.setReadOnly(true);
								tfMacAdress.setStyleName("caption-align-visualizar-conexao");
								
								addComponent(tfMacAdress);
								setExpandRatio(tfMacAdress, 2);						
							}
						});
						
						addComponent(new FormLayout(){					
							{
								setMargin(true);
								setSpacing(true);
								setStyleName("form-cutom-new");		
								
								TextField txtStatusPlano= new TextField("Status Plano");				
								txtStatusPlano.setWidth("80px");			

								
								txtStatusPlano.setValue(RadUserGroupDAO.getPlanoByUsername(ac.getLogin()));												
																
								txtStatusPlano.setReadOnly(true);
								txtStatusPlano.setStyleName("caption-align-visualizar-UpDown");
								
								if(!txtStatusPlano.getValue().equals("BLOQUEADO")){
									txtStatusPlano.addStyleName("valid-cpf");
								}else{
									txtStatusPlano.addStyleName("invalid-cpf");
								}
								
								addComponent(txtStatusPlano);
								setExpandRatio(txtStatusPlano, 2);						
							}
						});
					}
				});
				
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfInterface= new TextField("VLAN");				
						tfInterface.setWidth("300px");			

						if(regTable!=null && regTable.get("interface") !=null){	
							tfInterface.setValue(regTable.get("interface"));												
						}
											
						tfInterface.setReadOnly(true);
						tfInterface.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfInterface);
						setExpandRatio(tfInterface, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfTxRx= new TextField("Signal Strength (dBm)");				
						tfTxRx.setWidth("150px");			

						//TODO revisar busaca
						if(regTable!=null && regTable.get("signal-strength") !=null && regTable.get("signal-strength") !=null){	
													
							tfTxRx.setValue(regTable.get("signal-strength"));												
						}
						
						tfTxRx.setReadOnly(true);
						tfTxRx.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfTxRx);
						setExpandRatio(tfTxRx, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						//TODO revisar busaca
						TextField tfTxRxCCQ= new TextField("TX/RX CCQ (%)");				
						tfTxRxCCQ.setWidth("150px");			
						
						if(regTable!=null && regTable.get("tx-rate") !=null && regTable.get("rx-rate")!=null){
							tfTxRxCCQ.setValue(regTable.get("tx-ccq")+"/"+regTable.get("rx-ccq"));												
						}
						
						tfTxRxCCQ.setReadOnly(true);
						tfTxRxCCQ.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfTxRxCCQ);
						setExpandRatio(tfTxRxCCQ, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfDistanceKm= new TextField("Distance (km)");				
						tfDistanceKm.setWidth("150px");			

						if(regTable!=null && regTable.get("distance") !=null){	
							tfDistanceKm.setValue(regTable.get("distance"));												
						}
						
						tfDistanceKm.setReadOnly(true);
						tfDistanceKm.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfDistanceKm);
						setExpandRatio(tfDistanceKm, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfTxRxRate= new TextField("TXRX Rate");				
						tfTxRxRate.setWidth("150px");			

						if(regTable!=null && regTable.get("tx-rate")!=null){	
							tfTxRxRate.setValue(regTable.get("tx-rate")+"/"+regTable.get("rx-rate"));												
						}
						
						tfTxRxRate.setReadOnly(true);
						tfTxRxRate.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfTxRxRate);
						setExpandRatio(tfTxRxRate, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfPthrougtput= new TextField("P Througtput (kpps)");				
						tfPthrougtput.setWidth("150px");			

						if(regTable!=null && regTable.get("p-throughput")!=null){	
							tfPthrougtput.setValue(regTable.get("p-throughput"));												
						}
						
						tfPthrougtput.setReadOnly(true);
						tfPthrougtput.setStyleName("caption-align-visualizar-conexao");
						
						addComponent(tfPthrougtput);
						setExpandRatio(tfPthrougtput, 2);						
					}
				});	
			}
		};
		
		return vlconexao;
	}
	String carencia = "";
	public Table buildLayout(){
		
		//fieldGroup = new FieldGroup(item);

		
		VerticalLayout vlInformacoes = new VerticalLayout(){
			{				
				setMargin(true);
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfPlanoAtual = new TextField("Plano Atual");				
						tfPlanoAtual.setWidth("185px");			
						if(ac.getPlano().getNome()!=null){

							tfPlanoAtual.setValue(ac.getPlano().getNome());												
						}
						tfPlanoAtual.setReadOnly(true);
						tfPlanoAtual.setStyleName("caption-align-fornecedores");
						
						addComponent(tfPlanoAtual);
						setExpandRatio(tfPlanoAtual, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfUploadDownload = new TextField("Upload/Download");				
						tfUploadDownload.setWidth("120px");			
						if(ac.getPlano()!=null){

							String rateLimit[] = ac.getPlano().getRate_limit().toString().split("/");
							String up = rateLimit[0];
							String down = rateLimit[1];
							tfUploadDownload.setValue(up+"/"+down);												
						}
						tfUploadDownload.setReadOnly(true);
						tfUploadDownload.setStyleName("caption-align-fornecedores");
						
						addComponent(tfUploadDownload);
						setExpandRatio(tfUploadDownload, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfBaseUser = new TextField("Usuário");				
						tfBaseUser.setWidth("120px");			
						if(ac!=null){
							
							tfBaseUser.setValue(ac.getLogin());												
						}
						tfBaseUser.setReadOnly(true);
						tfBaseUser.setStyleName("caption-align-fornecedores");
						
						addComponent(tfBaseUser);
						setExpandRatio(tfBaseUser, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfBaseSenha= new TextField("Senha");				
						tfBaseSenha.setWidth("120px");			
						if(ac!=null){
							
							tfBaseSenha.setValue(ac.getSenha());												
						}
						tfBaseSenha.setReadOnly(true);
						tfBaseSenha.setStyleName("caption-align-fornecedores");
						
						addComponent(tfBaseSenha);
						setExpandRatio(tfBaseSenha, 2);						
					}
				});
				

				
			
				
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");										
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfSwithl= new TextField("CTO");				
						tfSwithl.setWidth("200px");			
						if(ac.getSwith()!=null){
							
							tfSwithl.setValue(ac.getSwith().getIdentificacao());												
						}
						tfSwithl.setReadOnly(true);
						tfSwithl.setStyleName("caption-align-fornecedores");
						
						addComponent(tfSwithl);
						setExpandRatio(tfSwithl, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfConcentrador= new TextField("Concentrador");				
						tfConcentrador.setWidth("200px");			
						if(ac.getBase()!=null){
							
							tfConcentrador.setValue(ac.getSwith().getConcentrador().getIdentificacao());												
						}
						tfConcentrador.setReadOnly(true);
						tfConcentrador.setStyleName("caption-align-fornecedores");
						
						addComponent(tfConcentrador);
						setExpandRatio(tfConcentrador, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfInterface= new TextField("VLAN");				
						tfInterface.setWidth("300px");			
						if(ac.getInterfaces()!=null){
							
							if(vlan != null){
								if(!vlan.equals(ac.getSwith().getInterfaces())){
									tfInterface.setValue(ac.getSwith().getInterfaces()+" ( DIVERGENTE )");
								}else{
									tfInterface.setValue(ac.getSwith().getInterfaces());
								}
							}else{
								tfInterface.setValue(ac.getSwith().getInterfaces());
							}
							
						}
						tfInterface.setReadOnly(true);
						tfInterface.setStyleName("caption-align-fornecedores");
						
						if(vlan != null){
							if(!vlan.equals(ac.getSwith().getInterfaces())){
								tfInterface.addStyleName("invalid-cpf");
							}
						}
						
						
						addComponent(tfInterface);
						setExpandRatio(tfInterface, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfOnu= new TextField("Olt");				
						tfOnu.setWidth("220px");			
						if(ac.getMaterial()!=null){
							
							tfOnu.setValue(ac.getSwith() != null && ac.getSwith().getOlt() != null ? ac.getSwith().getOlt() : "");												
						}
						tfOnu.setReadOnly(true);
						tfOnu.setStyleName("caption-align-fornecedores");
						
						addComponent(tfOnu);
						setExpandRatio(tfOnu, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfOnu= new TextField("Gpon");				
						tfOnu.setWidth("220px");			
						if(ac.getMaterial()!=null){
							
							tfOnu.setValue(ac.getSwith() != null && ac.getSwith().getPon() != null && ac.getGpon() != null ? ac.getSwith().getPon() +"/"+ ac.getGpon() : "");												
						}
						tfOnu.setReadOnly(true);
						tfOnu.setStyleName("caption-align-fornecedores");
						
						addComponent(tfOnu);
						setExpandRatio(tfOnu, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");										
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfSignal= new TextField("Signal Strength");				
						tfSignal.setWidth("200px");			
						if(ac.getSignal_strength()!=null){
							
							tfSignal.setValue(ac.getSignal_strength());												
						}
						tfSignal.setReadOnly(true);
						tfSignal.setStyleName("caption-align-fornecedores");
						
						addComponent(tfSignal);
						setExpandRatio(tfSignal, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");										
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfMaterial= new TextField("Material");				
						tfMaterial.setWidth("450px");			
						if(ac.getMaterial()!=null){
							
							tfMaterial.setValue(ac.getMaterial().getNome());												
						}
						tfMaterial.setReadOnly(true);
						tfMaterial.setStyleName("caption-align-fornecedores");
						
						addComponent(tfMaterial);
						setExpandRatio(tfMaterial, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfEndMac= new TextField("Endereço MAC");				
						tfEndMac.setWidth("200px");			
						if(ac.getEndereco_mac()!=null){
							
							tfEndMac.setValue(ac.getEndereco_mac());												
						}
						tfEndMac.setReadOnly(true);
						tfEndMac.setStyleName("caption-align-fornecedores");
						
						addComponent(tfEndMac);
						setExpandRatio(tfEndMac, 2);						
					}
				});	
				
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom-new");		
						
						TextField tfOnu= new TextField("Onu");				
						tfOnu.setWidth("220px");			
						if(ac.getMaterial()!=null){
							
							tfOnu.setValue(ac.getOnu() != null ? ac.getOnu().getNome() : "");												
						}
						tfOnu.setReadOnly(true);
						tfOnu.setStyleName("caption-align-fornecedores");
						
						addComponent(tfOnu);
						setExpandRatio(tfOnu, 2);						
					}
				});
				
				addComponent(new FormLayout(){					
					{
						setMargin(true);
						setSpacing(true);
						setStyleName("form-cutom");		
						
						TextField tfOnu= new TextField("Onu Serial");				
						tfOnu.setWidth("220px");			
						if(ac.getMaterial()!=null){
							
							tfOnu.setValue(ac.getOnu_serial() != null ? ac.getOnu_serial() : "");												
						}
						tfOnu.setReadOnly(true);
						tfOnu.setStyleName("caption-align-fornecedores");
						
						addComponent(tfOnu);
						setExpandRatio(tfOnu, 2);						
					}
				});
				
				
			}				
		};
		
		
		vlRootConexao = new VerticalLayout(){
			{
				addComponent(buildVlConecao());
			}
		};
		
		vlRootConsumo = new VerticalLayout(){
			{
				addComponent(buildVlConsumo());
			}
		};
		

		tbRoot = new TabSheet();
		tbRoot.setSizeFull();
	
		tbRoot.addTab(vlInformacoes,"Informações Técnicas");
		tbRoot.addTab(vlRootConexao,"Conexão");
		tbRoot.addTab(vlRootConsumo,"Gráficos");
		tbRoot.setStyleName("caption-align-fornecedores-bottom");
		
		vlRoot.addComponent(tbRoot);
		return null;
		
		}

		private Component buildBtAtualizar() {
			btAtualizar = new Button("Atualizar", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					vlRootConexao.replaceComponent(vlconexao, buildVlConecao());		
					
					calcularMulta();
					
				}
			});
			return btAtualizar;
		}
		
		private void calcularMulta(){
			//-----Calcular multa,beneficio;
			//----------------------------------------------
			String carencia = "";
			Integer months = 0;
			if(ac.getCarencia() != null && ac.getCarencia().equals("SIM") && ac.getContrato().getCarencia().equals("SIM")){			
				
				try{
					SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");				
					DateTime dt1 = new DateTime(sdf.parse(sdf.format(new Date())));
					DateTime dt2 = new DateTime(sdf.parse(sdf.format(ac.getData_venc_contrato()))).plusMonths(1);			 
					months = Months.monthsBetween(dt1, dt2).getMonths();				
					carencia = months.toString()+" MESES";
				}catch(Exception e){
					e.printStackTrace();
				}
				
				if(months > 0){
					carencia = months.toString()+" MESES";
				}else{
					carencia = "SEM CARENCIA";
				}
			}else{
				carencia = "SEM CARENCIA";
			}
			
			String bc = "0,00";
			String bi = "0,00";
			
			if(!carencia.equals("SEM CARENCIA")){
				bc = Real.formatDbToString(String.valueOf(ac.getValor_beneficio_comodato()));												
			}
			
			if(ac.getRegime().equals("PROPRIO")){
				bc = "0,00";
			}
			
			if(ac.getInstalacao_gratis() != null && ac.getInstalacao_gratis().equals("SIM")){
				
				if(ac.getCarencia() != null && ac.getCarencia().equals("SIM")){		

					EntityManager em = ConnUtil.getEntity();
					Query qValorBenfInst = em.createNativeQuery("SELECT ts.VALOR FROM ose o, tipos_ose g, tipos_subgrupo ts WHERE "
							+ "o.GRUPO_ID = g.ID AND ts.ID = "
							+ "o.TIPO_SUBGRUPO_ID AND g.NOME LIKE '%INSTALACAO%' AND o.ACESSO_CLIENTE_ID = :contrato ");
					qValorBenfInst.setParameter("contrato", ac.getId());
					
					if(qValorBenfInst.getResultList().size() > 0){
						bi = Real.formatDbToString(qValorBenfInst.getResultList().toArray()[0].toString());
					}
				}else{
					bi = "0,00";
				}
			}else{
				bi = "0,00";
			}
			
			int qtd_boletos_abertos = ContasReceberDAO.procurarBoletosDoAcessoPorContrato(ac.getId()) != null ? ContasReceberDAO.procurarBoletosDoAcessoPorContrato(ac.getId()).size() : 0;
			double valor_multa = 0;
			double valor_adesao = !carencia.equals("SEM CARENCIA") ? ac.getValor_beneficio_adesao() : 0;
											 //!carencia.equals("SEM CARENCIA")?Real.formatDbToString(String.valueOf(ac.getValor_beneficio_adesao())):"0,00"
			double valor_equipamento = Real.formatStringToDBDouble(bc);
			double valor_instalacao =  Real.formatStringToDBDouble(bi);
			double valor_total = valor_adesao+valor_equipamento+valor_instalacao;
			
			if(qtd_boletos_abertos >= 12){
				valor_multa = valor_total;
			}else if(qtd_boletos_abertos == 11){
				valor_multa = (valor_total*91.67)/100;
			}else if(qtd_boletos_abertos == 10){
				valor_multa = (valor_total*83.34)/100;
			}else if(qtd_boletos_abertos == 9){
				valor_multa = (valor_total*75.01)/100;
			}else if(qtd_boletos_abertos == 8){
				valor_multa = (valor_total*66.68)/100;
			}else if(qtd_boletos_abertos == 7){
				valor_multa = (valor_total*58.35)/100;
			}else if(qtd_boletos_abertos == 6){
				valor_multa = (valor_total*50.02)/100;
			}else if(qtd_boletos_abertos == 5){
				valor_multa = (valor_total*41.69)/100;
			}else if(qtd_boletos_abertos == 4){
				valor_multa = (valor_total*33.36)/100;
			}else if(qtd_boletos_abertos == 3){
				valor_multa = (valor_total*25.03)/100;
			}else if(qtd_boletos_abertos == 2){
				valor_multa = (valor_total*16.70)/100;
			}else if(qtd_boletos_abertos == 1){
				valor_multa = (valor_total*8.37)/100;
			}
			
			//Benefício Adesão
			System.out.println("Adesão: "+Real.formatDbToString(String.valueOf(valor_adesao)));
			//Benefício Comodato
			System.out.println("Equipamento: "+Real.formatDbToString(String.valueOf(valor_equipamento)));
			//Benfício Instalação
			System.out.println("Valor Instalacao: "+Real.formatDbToString(String.valueOf(valor_instalacao)));
			//Multa Recisória
			System.out.println("Multa: "+Real.formatDbToString(String.valueOf(valor_multa)));
			
			//Valor total
			System.out.println("Valor Total: "+Real.formatDbToString(String.valueOf(valor_total)));
			
		}
				
		private Component buildBtCancelar() {
			btCancelar = new Button("Fechar", new Button.ClickListener() {
				
				@Override
				public void buttonClick(ClickEvent event) {
					close();
				}
			});
			
			ShortcutListener slbtCancelar = new ShortcutListener("Cancelar", ShortcutAction.KeyCode.ESCAPE,null) {
				
				@Override
				public void handleAction(Object sender, Object target) {
					btCancelar.click();
				}
			};
			btCancelar.addShortcutListener(slbtCancelar);
			return btCancelar;
		}
		
}
	
