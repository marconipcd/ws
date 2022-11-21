package com.digital.opuserp.view.modulos.acesso.contratos_acesso;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import com.digital.opuserp.OpusERP4UI;
import com.digital.opuserp.dao.CredenciaisAcessoDAO;
import com.digital.opuserp.dao.SwithDAO;
import com.digital.opuserp.domain.AcessoCliente;
import com.digital.opuserp.domain.Cliente;
import com.digital.opuserp.domain.Concentrador;
import com.digital.opuserp.domain.RadAcct;
import com.digital.opuserp.domain.Swith;
import com.digital.opuserp.interfaces.GenericEditor;
import com.digital.opuserp.util.ConnUtil;
import com.digital.opuserp.util.GenericDialog;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.util.HuaweiUtil;
import com.digital.opuserp.util.MikrotikUtil;
import com.digital.opuserp.util.SignalStrengthUtil;
import com.digital.opuserp.util.mk_bean.WirelessInterface;
import com.digital.opuserp.view.modulos.cadastro.transportadoras.TransportadoraEditor.TransportadoraEvent;
import com.digital.opuserp.view.util.Notify;
import com.vaadin.addon.jpacontainer.EntityItem;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.fieldfactory.SingleSelectConverter;
import com.vaadin.addon.jpacontainer.filter.Filters;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MigrarRadio extends Window implements GenericEditor{

	Item item;
	Button btSalvar; 
	Button btCancelar;
	Button btAtualizar;
	FormLayout flPrincipal;
	FieldGroup fieldGroup;
	VerticalLayout vlRoot;
	
		
	boolean valid_mac = false;
	
	
	boolean valid_senha = false;
	boolean Validar_signal = false;
	boolean conectado = false;
	boolean conexao = false;
	

	TextField tfConexao; 
	TextField tfComunicacao; 
	TextField tfEnderecoIPPool; 
	EntityManager em = ConnUtil.getEntity();
	Integer codAcesso;
	AcessoCliente ac;			
	Concentrador base; 
	
	Concentrador concentrador;
	ComboBox cbInterface;
	ComboBox cbConcentradores;
	Integer vez = 0;
	String ipAtual;
	
	String loginAtual;
	
	private ComboBox cbSwitch;
	private TextField txtSinalDbCaixa;
	
	String[] info = null;
	String tipo_concentrador_selecionado = "";
	
	public MigrarRadio(Item item, String title, boolean modal){
		this.item = item;
		
		if(item.getItemProperty("id").getValue()!=null){
			codAcesso = (Integer) item.getItemProperty("id").getValue();
			ac = em.find(AcessoCliente.class, codAcesso);				
			base = (Concentrador) item.getItemProperty("base").getValue();	
			
			
			if(base != null && base.getTipo().equals("huawei")){
				info = HuaweiUtil.pegarInformacoes(ac.getLogin());
			}			
		}
		
		if (item.getItemProperty("endereco_ip").getValue() != null) {
			ipAtual = item.getItemProperty("endereco_ip").getValue().toString();
		}
		
		
		setWidth("683px");
		setHeight("370px");

		setCaption(title);
		setModal(modal);
		setResizable(false);
		setClosable(false);
		center();
		
		vlRoot = new VerticalLayout();	
		vlRoot.setWidth("100%");
		vlRoot.setMargin(true);
		vlRoot.setStyleName("border-form");
		
		setContent(new VerticalLayout(){
			{
				setWidth("100%");
				setMargin(true);
				addComponent(vlRoot);
				
				
				HorizontalLayout hlButtons = new HorizontalLayout();
				hlButtons.setStyleName("hl_buttons_bottom");
				hlButtons.setSpacing(true);
				hlButtons.setMargin(true);
				hlButtons.addComponent(buildBtCancelar());
				hlButtons.addComponent(buildBtSalvar());
				
				addComponent(hlButtons);
				setComponentAlignment(hlButtons, Alignment.BOTTOM_RIGHT);
			}
		});
		
		
		if (item.getItemProperty("login").getValue() != null) {
			loginAtual = item.getItemProperty("login").getValue().toString();
		}
		
		buildLayout();
	}
	
	
	public void buildLayout(){
		
		fieldGroup = new FieldGroup(item);
		
		final Cliente cliente = (Cliente) item.getItemProperty("cliente").getValue();
		
		vlRoot.addComponent(	new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
					
					TextField tfCodContrato = new TextField("Contrato");
					tfCodContrato.setValue(item.getItemProperty("id").getValue().toString());
					tfCodContrato.setReadOnly(true);
					tfCodContrato.setWidth("62px");
					tfCodContrato.setStyleName("caption-align-contrato");
					
					
					addComponent(tfCodContrato);
				}
		});
		vlRoot.addComponent(	new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);

					TextField tfNomeCliente = new TextField("Cliente");
					tfNomeCliente.setValue(cliente.getNome_razao());
					tfNomeCliente.setReadOnly(true);
					tfNomeCliente.setWidth("360px");
					tfNomeCliente.setStyleName("caption-align-contrato");
					
					addComponent(tfNomeCliente);
				}
		});
		vlRoot.	addComponent(new FormLayout(){					
			{
				setMargin(true);
				setSpacing(true);
				setStyleName("form-cutom-new");		
				
				TextField tfStatusConcent= new TextField("Status Concentrador");				
				tfStatusConcent.setWidth("150px");	
				tfStatusConcent.setStyleName("caption-align-contrato");
				
				boolean teste =false;
				
				if(base.getTipo().equals("mikrotik")){
					if(base!=null){
						teste = MikrotikUtil.testconexao(base.getUsuario(),base.getSenha(),base.getEndereco_ip(),Integer.parseInt(base.getPorta_api()));
					}
				}
				
				if(base.getTipo().equals("huawei")){
					if(info != null){
						teste = true;
					}
				}

				if(teste){						
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
		vlRoot.addComponent(new FormLayout(){					
			{
				setStyleName("form-cutom-new");
				setMargin(true);
				setSpacing(true);
				
				tfComunicacao = new TextField("Comunicação Concentrador"); 
				tfComunicacao.setStyleName("caption-align-contrato");
				tfComunicacao.setReadOnly(true);		
				
				addComponent(tfComunicacao);
								
				
				
				if(base.getTipo().equals("mikrotik")){
					conexao = MikrotikUtil.buscarComunicacao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getEndereco_mac());
					conectado = MikrotikUtil.buscarStatusConexao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getLogin());
				}
				
				if(base.getTipo().equals("huawei") && info != null){
					conectado = true;
					conexao = true;
				}
				
				
				if(conexao || conectado){
					tfComunicacao.setReadOnly(false);						
					tfComunicacao.setValue("ESTABELECIDA");
					tfComunicacao.addStyleName("valid-cpf");
					tfComunicacao.setReadOnly(true);										
				}else{
					tfComunicacao.setReadOnly(false);						
					tfComunicacao.setValue("NÃO ESTABELECIDA");	
					tfComunicacao.addStyleName("invalid-cpf");
					tfComunicacao.setReadOnly(true);												
				}
			}
		});
		vlRoot.addComponent(new HorizontalLayout(){					
			{				
				tfConexao = new TextField("Status da Conexão"); 
				tfConexao.setStyleName("caption-align-contrato");
				tfConexao.setReadOnly(true);							
							
				if(conectado){
					tfConexao.setReadOnly(false);						
					tfConexao.setValue("CONECTADO");
					tfConexao.addStyleName("valid-cpf");
					tfConexao.setReadOnly(true);						
				}else{
					tfConexao.setReadOnly(false);						
					tfConexao.setValue("DESCONECTADO");		
					tfConexao.addStyleName("invalid-cpf");					
					tfConexao.setReadOnly(true);						
				}
				
				FormLayout flConectado = new FormLayout(){
					{	
						setStyleName("form-cutom");
						setMargin(true);
						setSpacing(true);							
						addComponent(tfConexao);							
					}
				};
				
				addComponent(flConectado);
				
				btAtualizar = new Button("Atualizar", new Button.ClickListener() {
					
					@Override
					public void buttonClick(ClickEvent event) {	
						
						boolean conexao = false; 
						
						if(base.getTipo().equals("mikrotik")){
							conexao = MikrotikUtil.buscarComunicacao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getEndereco_mac());
							conectado = MikrotikUtil.buscarStatusConexao(base.getUsuario(), base.getSenha(),base.getEndereco_ip(), Integer.parseInt(base.getPorta_api()), ac.getLogin());
						}
						
						if(base.getTipo().equals("huawei") && info != null){
							conectado = true;
							conexao = true;
						}
					
						if(conexao || conectado){
							tfComunicacao.setReadOnly(false);						
							tfComunicacao.setValue("ESTABELECIDA");
							tfComunicacao.addStyleName("valid-cpf");
							tfComunicacao.setReadOnly(true);										
						}else{
							tfComunicacao.setReadOnly(false);						
							tfComunicacao.setValue("NÃO ESTABELECIDA");	
							tfComunicacao.addStyleName("invalid-cpf");
							tfComunicacao.setReadOnly(true);												
						}
						
						if(conectado){
							tfConexao.setReadOnly(false);						
							tfConexao.setValue("CONECTADO");
							tfConexao.addStyleName("valid-cpf");
							tfConexao.setReadOnly(true);						
						}else{
							tfConexao.setReadOnly(false);						
							tfConexao.setValue("DESCONECTADO");		
							tfConexao.addStyleName("invalid-cpf");					
							tfConexao.setReadOnly(true);						
						}
						
					}
				});	
				
				btAtualizar.setStyleName("atualizar");
				
				FormLayout flmBt = new FormLayout(){
					{					
						setMargin(true);
						setSpacing(true);	
						setStyleName("form-cutom");
						addComponent(btAtualizar);							
					}
				};
				
				addComponent(flmBt);
			}
		});
		vlRoot.addComponent(new FormLayout(){					
				{
				setStyleName("form-cutom");
				setMargin(true);
				setSpacing(true);
				
				tfEnderecoIPPool = new TextField("Endereço IP Pool"); 
				tfEnderecoIPPool.setStyleName("caption-align-contrato");
				tfEnderecoIPPool.setReadOnly(true);						
				
				addComponent(tfEnderecoIPPool);		
				
				CredenciaisAcessoDAO cDAO = new CredenciaisAcessoDAO();
				
				RadAcct radacct = cDAO.buscarRadacct(ac.getLogin());
				
				if(radacct!=null && !radacct.equals("")){
					tfEnderecoIPPool.setReadOnly(false);		
								
						tfEnderecoIPPool.setValue(radacct.getFramedipaddress());										
						BrowserWindowOpener openIpPool = new BrowserWindowOpener("http://"+radacct.getFramedipaddress());
						openIpPool.setFeatures("height=600,width=800");
						openIpPool.extend(tfEnderecoIPPool);

		
					tfEnderecoIPPool.setValue(radacct.getFramedipaddress());
					tfEnderecoIPPool.setReadOnly(true);						
				}else{
					tfEnderecoIPPool.setReadOnly(false);						
					tfEnderecoIPPool.setValue("");						
					tfEnderecoIPPool.setReadOnly(true);						
				}
			}
		});
		
		vlRoot.addComponent(new HorizontalLayout(){
			{
				addComponent(new FormLayout(){					
						{
							setStyleName("form-cutom");
							setMargin(true);
							setSpacing(true);
								
							JPAContainer<Concentrador> containerConcentradores = JPAContainerFactory.make(Concentrador.class, ConnUtil.getEntity());
							containerConcentradores.addContainerFilter(Filters.eq("empresa_id", OpusERP4UI.getEmpresa().getId()));
							containerConcentradores.addContainerFilter(Filters.eq("status", "ATIVO"));
							containerConcentradores.sort(new Object[]{"identificacao"}, new boolean[]{true});
							
							cbConcentradores = new ComboBox("Concentrador", containerConcentradores);
							cbConcentradores.setNullSelectionAllowed(false);
							cbConcentradores.setRequired(true);
							cbConcentradores.setItemCaptionPropertyId("identificacao");
							cbConcentradores.setConverter(new SingleSelectConverter(cbConcentradores));
							cbConcentradores.setStyleName("caption-align-contrato");
							cbConcentradores.setImmediate(true);
							
											
							cbConcentradores.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									
									if(vez > 0){
										try{						
											String usuario = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("usuario").getValue().toString();
											String tipo = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("tipo").getValue().toString();
											
											tipo_concentrador_selecionado = tipo;
											
											String senha = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("senha").getValue().toString();
											String ip = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("endereco_ip").getValue().toString();
											Integer porta = Integer.parseInt(cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("porta_api").getValue().toString());
											String fibra = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("fibra").getValue() != null && 
													!cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("fibra").getValue().toString().equals("") ? 
															cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("fibra").getValue().toString() : "NAO";
															
											if(fibra.equals("SIM")){
												txtSinalDb.setEnabled(true);
												txtSinalDb.setRequired(true);
												
												txtGpon.setEnabled(true);
												txtGpon.setRequired(true);
											}else{
												txtSinalDb.setEnabled(true);
												txtSinalDb.setValue("");
												txtSinalDb.setEnabled(false);
												txtSinalDb.setRequired(false);
												
												txtGpon.setEnabled(true);
												txtGpon.setValue("");
												txtGpon.setEnabled(false);
												txtGpon.setRequired(false);
											}
											
											
											if(tipo.equals("mikrotik")){
													List<Map<String, String>> result  = MikrotikUtil.listarInterfacesWireless(usuario, senha, ip, porta);
													
													if(result != null){
															cbInterface.removeAllItems();
															for (Map<String, String> wirelessInterface : result) {
																String mtu = wirelessInterface.get("mtu");
																if(mtu.equals("1500")){						
																	cbInterface.addItem(wirelessInterface.get("name") != null ? wirelessInterface.get("name") : "");
																}																
															}																								
															if(item.getItemProperty("interfaces").getValue() != null){
																cbInterface.select(item.getItemProperty("interfaces").getValue().toString());
															}
													}
											}else{
												cbInterface.setRequired(false); 
											}
											
											
											Integer codConcentrador = (Integer)cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("id").getValue();
											List<Swith> swiths = SwithDAO.getSwithsbyConcentrador(codConcentrador);
											
											if(swiths != null && swiths.size() > 0){
																													
												cbSwitch.setEnabled(true);
												cbSwitch.setRequired(true);
												cbSwitch.setContainerDataSource(biuldContainerSwith(codConcentrador));
												cbSwitch.setItemCaptionPropertyId("identificacao");
												cbSwitch.setConverter(new SingleSelectConverter(cbSwitch));
												fieldGroup.bind(cbSwitch, "swith");
												
											}else{
												cbSwitch.setEnabled(false);
												cbSwitch.setRequired(false);
											}
																		
										}catch(Exception e){ 
											e.printStackTrace();								
											Notify.Show("ERRO: "+e.getLocalizedMessage(), Notify.TYPE_ERROR);
										}
									}
									vez++;
									
								}
							});
							
							addComponent(cbConcentradores);		
							
							fieldGroup.bind(cbConcentradores, "base");
							
							EntityItem<Concentrador> eItem = (EntityItem<Concentrador>)cbConcentradores.getItem(cbConcentradores.getValue());
							concentrador = eItem.getEntity();
						}
					});
				
				
				
			}
		});
		vlRoot.addComponent(new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					
					final BeanItemContainer<WirelessInterface> interfaces = new BeanItemContainer<>(WirelessInterface.class);
					List<Map<String, String>> result = null;
					if(cbConcentradores.getValue() != null){
						String usuario = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("usuario").getValue().toString();
						String senha = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("senha").getValue().toString();
						String ip = cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("endereco_ip").getValue().toString();
						Integer porta = Integer.parseInt(cbConcentradores.getItem(cbConcentradores.getValue()).getItemProperty("porta_api").getValue().toString());
						
						result  = MikrotikUtil.listarInterfacesWireless(usuario, senha, ip, porta);				
					}
					
					
					cbInterface = new ComboBox("Interface");
					cbInterface.setNullSelectionAllowed(false);
					
					if(base != null && base.getTipo().equals("mikrotik")){
						cbInterface.setRequired(true);
					}else{
						cbInterface.setRequired(false);
					}
					
					
					//cbInterface.setItemCaptionPropertyId("name");					
					cbInterface.setStyleName("caption-align-contrato");
					//cbInterface.setConverter(new SingleSelectConverter(null));
					if(result != null){
						
						if(result.size() >0){
							for (Map<String, String> wirelessInterface : result) {
								
								String mtu = wirelessInterface.get("mtu");
								if(mtu != null && mtu.equals("1500")){						
									cbInterface.addItem(wirelessInterface.get("name"));
								}

							}
							
							if(item.getItemProperty("interfaces").getValue() != null){
								cbInterface.select(item.getItemProperty("interfaces").getValue().toString());						
							}
						}
					}
										
					addComponent(cbInterface);
					
									
					
					
					//fieldGroup.bind(cbInterface, "interfaces");
				}
			});
		
	}
	
	TextField txtGpon;
	TextField txtSinalDb;
	TextField txtPON;
	TextField txtOLT;
	
	private JPAContainer<Swith> biuldContainerSwith(Integer codConcentrador){
		JPAContainer<Swith> swithsContainer = JPAContainerFactory.make(Swith.class, ConnUtil.getEntity());
		swithsContainer.addContainerFilter(Filters.eq("concentrador", new Concentrador(codConcentrador)));
		swithsContainer.sort(new Object[]{"identificacao"}, new boolean[]{true});
		return swithsContainer;
	}

	@Override
	public Button buildBtSalvar() {
		
		btSalvar = new Button("OK", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				concluirAlteracaoConcentrador();			
			}
		});
		

		ShortcutListener slbtOK = new ShortcutListener("Ok", ShortcutAction.KeyCode.ENTER,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btSalvar.click();
			}
		};
		
		btSalvar.addShortcutListener(slbtOK);		
		btSalvar.setStyleName("default");
		//btSalvar.setEnabled(false);
		return btSalvar;
	}
	
	private void concluirAlteracaoConcentrador(){
		if(fieldGroup.getField("signal_strength").getValue()!= null && !fieldGroup.getField("signal_strength").getValue().equals("")){

			boolean valid = SignalStrengthUtil.validSignalStrength(fieldGroup.getField("signal_strength").getValue().toString());
			if(valid){
				btSalvar.setStyleName("valid-cpf");
				Validar_signal = true;
			}else{
				btSalvar.setStyleName("invalid-cpf");
				Validar_signal = false;	
			}
		}
		
		if(fieldGroup.getField("signal_strength").getValue() == null || fieldGroup.getField("signal_strength").getValue().equals("")){
			Validar_signal = true;
		}
		
		//if(fieldGroup.getField("endereco_ip").getValue() == null || fieldGroup.getField("endereco_ip").getValue().toString().equals("")){
		//	valid_ip = true;
		//}
		
		
		
		if(fieldGroup.isValid() && cbInterface.isValid() && Validar_signal){
			try {												
				fieldGroup.commit();
				
				if(tipo_concentrador_selecionado.equals("mikrotik")){
					item.getItemProperty("interfaces").setValue(cbInterface.getValue().toString());
				}

				if(fieldGroup.getField("signal_strength").getValue() == null || fieldGroup.getField("signal_strength").getValue().equals("")){
					item.getItemProperty("signal_strength").setValue("-120..120");
					Validar_signal = true;
				}
				
				if(!cbSwitch.isEnabled()){
					item.getItemProperty("swith").setValue(null);
				}
					
				fireEvent(new MigrarRadioEvent(getUI(), item, true));	
					
				
			} catch (Exception e) {											
				Notify.Show("ERRO: "+e.getLocalizedMessage(), Notify.TYPE_ERROR);		
				e.printStackTrace();
			}				
		}else{
			
			for (Field<?> field: fieldGroup.getFields()) {
				
				if(!field.isValid()){
					field.addStyleName("invalid-txt");
				}else{
					field.removeStyleName("invalid-txt");
				}
				
				if(!cbInterface.isValid()){
					cbInterface.addStyleName("invalid-txt");
				}else{
					cbInterface.removeStyleName("invalid-txt");
				}
				
			}
			
				Notify.Show_Invalid_Submit_Form();				
		}
	}

	@Override
	public Button buildBtCancelar() {
		btCancelar = new Button("Cancelar", new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if(!fieldGroup.isModified()){
					fieldGroup.discard();				
					fireEvent(new MigrarRadioEvent(getUI(), item, false));
					close();
				}else{
					GenericDialog gDialog = new GenericDialog("Confirme para Continuar!", "Deseja Salvar as Informações Alteradas?", true, true);
					gDialog.setCaptionCANCEL("Sair sem Salvar!");
					gDialog.setCaptionOK("Salvar");
					
					gDialog.addListerner(new GenericDialog.DialogListerner() {
						
						@Override
						public void onClose(DialogEvent event) {
							if(event.isConfirm()){								
								concluirAlteracaoConcentrador();
							}else{							
								fieldGroup.discard();				
								fireEvent(new TransportadoraEvent(getUI(), item, false));													
							}
						}
					});					
					
					getUI().addWindow(gDialog);
					
				}				
			}
		});
		
		ShortcutListener slbtCancelar = new ShortcutListener("Cancelar", ShortcutAction.KeyCode.ESCAPE,null) {
			
			@Override
			public void handleAction(Object sender, Object target) {
				btCancelar.click();
			}
		};
		
		btCancelar.addShortcutListener(slbtCancelar);
		btCancelar.setEnabled(true);
		
		return btCancelar;
	}
	
	
	public void addListerner(MigrarRadioListerner target){
		try {
			Method method = MigrarRadioListerner.class.getDeclaredMethod("onClose", MigrarRadioEvent.class);
			addListener(MigrarRadioEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("Método não Encontrado!");
		}
	}
	public void removeListerner(MigrarRadioListerner target){
		removeListener(MigrarRadioEvent.class, target);
	}
	public static class MigrarRadioEvent extends Event{
		
		private Item item;
		private boolean confirm;		
		
		public MigrarRadioEvent(Component source, Item item, boolean confirm) {
			super(source);
			this.item = item;
			this.confirm = confirm;			
		}

		public Item getItem() {
			return item;
		}	

		public boolean isConfirm() {
			return confirm;
		}		
		
	}
	public interface MigrarRadioListerner extends Serializable{
		public void onClose(MigrarRadioEvent event);
	}
}
