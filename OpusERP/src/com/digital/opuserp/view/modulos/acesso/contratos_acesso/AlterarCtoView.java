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
import com.digital.opuserp.util.HuaweiUtil;
import com.digital.opuserp.util.MikrotikUtil;
import com.digital.opuserp.util.SignalStrengthUtil;
import com.digital.opuserp.util.GenericDialog.DialogEvent;
import com.digital.opuserp.util.mk_bean.WirelessInterface;
import com.digital.opuserp.view.modulos.acesso.contratos_acesso.AlterarConcentradorAcessoView.AlterarConcentradorAcessoEvent;
import com.digital.opuserp.view.modulos.acesso.contratos_acesso.AlterarConcentradorAcessoView.AlterarConcentradorAcessoListerner;
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
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component.Event;

public class AlterarCtoView extends Window implements GenericEditor{

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
	TextField txtInterface;
	TextField txtConcentrador;
	Integer vez = 0;
	String ipAtual;
	
	String loginAtual;
	
	private ComboBox cbCto;
	private TextField txtSinalDbCaixa;
	
	String[] info = null;
	String tipo_concentrador_selecionado = "";
	
	public AlterarCtoView(Item item, String title, boolean modal){
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
		setHeight("550px");

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
							
							cbCto = new ComboBox("CTO");
						
							cbCto.setNullSelectionAllowed(false);						
							cbCto.setEnabled(true);
							cbCto.setRequired(true);
							cbCto.setContainerDataSource(biuldContainerSwith());
							cbCto.setItemCaptionPropertyId("identificacao");
							cbCto.setConverter(new SingleSelectConverter(cbCto));
							cbCto.setStyleName("caption-align-contrato");
							
							cbCto.addValueChangeListener(new Property.ValueChangeListener() {
								
								@Override
								public void valueChange(ValueChangeEvent event) {
									if(txtPON != null && 
										cbCto.getValue() != null && 
										cbCto.getItem(cbCto.getValue()).getItemProperty("pon").getValue() != null){
										
										EntityItem<Swith> eiCto = (EntityItem<Swith>)cbCto.getItem(cbCto.getValue());
										Swith s = eiCto.getEntity();
										
										
										txtPON.setEnabled(true);
										txtPON.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("pon").getValue().toString());
										txtPON.setEnabled(false);
										
										txtSinalDbCaixa.setReadOnly(false);
										txtSinalDbCaixa.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("sinal_db").getValue().toString());
										txtSinalDbCaixa.setReadOnly(true);
										
										
										txtConcentrador.setEnabled(true);
										txtConcentrador.setValue(s.getConcentrador().getIdentificacao());
										txtConcentrador.setEnabled(false);
										
										txtInterface.setEnabled(true);
										txtInterface.setValue(s.getInterfaces());
										txtInterface.setEnabled(false);
										
										
									}else{
										if(txtPON != null){
											txtPON.setEnabled(true);
											txtPON.setValue("");
											txtPON.setEnabled(false);
										}
									}
									
									if(txtOLT != null && cbCto.getValue() != null && cbCto.getItem(cbCto.getValue()).getItemProperty("olt").getValue() != null){
										txtOLT.setEnabled(true);
										txtOLT.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("olt").getValue().toString());
										txtOLT.setEnabled(false);
										
										txtSinalDbCaixa.setReadOnly(false);
										txtSinalDbCaixa.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("sinal_db").getValue().toString());
										txtSinalDbCaixa.setReadOnly(true);
									}else{
										if(txtOLT != null){
											txtOLT.setEnabled(true);
											txtOLT.setValue("");
											txtOLT.setEnabled(false);
										}
									}
								}
							});
							
							fieldGroup.bind(cbCto, "swith");				
							
							addComponent(cbCto);
											
						}
				}); 
			
			
				addComponent(new FormLayout(){
					{
						setStyleName("form-cutom");
						setMargin(true);
						setSpacing(true);
						
						txtSinalDbCaixa = new TextField();
						txtSinalDbCaixa.setWidth("50px");
						txtSinalDbCaixa.setReadOnly(true); 
						
						if(cbCto != null && cbCto.getValue() != null){
							txtSinalDbCaixa.setReadOnly(false);
							txtSinalDbCaixa.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("sinal_db").getValue().toString());
							txtSinalDbCaixa.setReadOnly(true);
						}
						
						addComponent(txtSinalDbCaixa); 
						
						Label lb = new Label("-");
						lb.setStyleName("lbMinusCaixaAtendimento");
						
						addComponent(lb); 
					}
				});
			
			}
		});	
		vlRoot.addComponent(new HorizontalLayout(){
			{
				addComponent(new FormLayout(){					
						{
							setStyleName("form-cutom");
							setMargin(true);
							setSpacing(true);
								
							txtConcentrador = new TextField("Concentrador");							
							txtConcentrador.setRequired(true);							
							txtConcentrador.setStyleName("caption-align-contrato");
							txtConcentrador.setEnabled(false); 
								
							addComponent(txtConcentrador);		
														
						}
					});			
			}
		});
		
		vlRoot.addComponent(new HorizontalLayout(){
			{
				addComponent(new FormLayout(){					
						{
							setStyleName("form-cutom");
							setMargin(true);
							setSpacing(true);
								
							txtInterface = new TextField("Interface");							
							txtInterface.setRequired(true);							
							txtInterface.setStyleName("caption-align-contrato");
							txtInterface.setEnabled(false); 
								
							addComponent(txtInterface);		
														
						}
					});			
			}
		});
		
		
		
		
		
		
		
		
		
		
		
		vlRoot.addComponent(new FormLayout(){					
			{
				setStyleName("form-cutom");
				setMargin(true);
				setSpacing(true);
				
				txtOLT = new TextField("OLT");
				txtOLT.setStyleName("caption-align-contrato");
				
				if(cbCto != null && cbCto.getValue() != null && cbCto.getItem(cbCto.getValue()) != null && 
						cbCto.getItem(cbCto.getValue()).getItemProperty("olt").getValue() != null){
					txtOLT.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("olt").getValue().toString());
				}
								
				txtOLT.setEnabled(false);
				
				addComponent(txtOLT);
								
			}
		});	
		
		
		
		
		vlRoot.addComponent(new FormLayout(){					
			{
				setStyleName("form-cutom");
				setMargin(true);
				setSpacing(true);
								
				txtPON = new TextField("GPON");
				txtPON.setStyleName("caption-align-concentrador-gpon");
				txtPON.setEnabled(false);
				txtPON.setWidth("37px");
				
				if(cbCto.getValue() != null && cbCto.getItem(cbCto.getValue()).getItemProperty("pon").getValue() != null){
					txtPON.setEnabled(true);
					txtPON.setValue(cbCto.getItem(cbCto.getValue()).getItemProperty("pon").getValue().toString());
					txtPON.setEnabled(false);
				}
								
				txtGpon = new TextField();
				txtGpon.setNullRepresentation("");			
				txtGpon.setMaxLength(3);
				txtGpon.setStyleName("input-concentrador-gpon-correcao");
				txtGpon.setWidth("75px");				
				txtGpon.setId("txtGpon");
				txtGpon.addBlurListener(new FieldEvents.BlurListener() {
					
					@Override
					public void blur(BlurEvent event) {
						if(txtGpon.getValue() != null && txtGpon.getValue().length()  == 1){
						
							txtGpon.setValue("0"+txtGpon.getValue());
						}
					}
				});
				
				
				JavaScript.getCurrent().execute("$('#txtGpon').mask('999')");
				
				
				FormLayout fl1 = new FormLayout();
				fl1.setStyleName("form-cutom");
				fl1.setMargin(true);
				fl1.setSpacing(true);
				fl1.addComponent(txtPON);
				
				FormLayout fl2 = new FormLayout();
				fl2.setStyleName("form-cutom");
				fl2.setMargin(true);
				fl2.setSpacing(true);
				fl2.addComponent(txtGpon);
						
				HorizontalLayout hl = new HorizontalLayout();
				hl.setWidth("100%");
				hl.setSpacing(false);
				hl.setMargin(false); 
				hl.addComponent(fl1);
				hl.addComponent(fl2);
							
				addComponent(hl);	
								
				fieldGroup.bind(txtGpon, "gpon");
			}
		});
		
		
		vlRoot.addComponent(new FormLayout(){					
				{
					setStyleName("form-cutom");
					setMargin(true);
					setSpacing(true);
					
					addComponent(fieldGroup.buildAndBind("Signal Strength", "signal_strength"));					
					((TextField)fieldGroup.getField("signal_strength")).setNullRepresentation("");
					((TextField)fieldGroup.getField("signal_strength")).setStyleName("caption-align-contrato");
					((TextField)fieldGroup.getField("signal_strength")).setId("txtStrength");
					((TextField)fieldGroup.getField("signal_strength")).setInputPrompt("-120..120");
					((TextField)fieldGroup.getField("signal_strength")).setMaxLength(9);
					((TextField)fieldGroup.getField("signal_strength")).setImmediate(true);
					((TextField)fieldGroup.getField("signal_strength")).addListener(new FieldEvents.TextChangeListener() {
						
						@Override
						public void textChange(TextChangeEvent event) {
								
								if(event.getText()!=null && !event.getText().equals("")){

									boolean valid = SignalStrengthUtil.validSignalStrength(event.getText());
									if(valid){
										event.getComponent().removeStyleName("invalid-cpf");
										event.getComponent().addStyleName("valid-cpf");
										Validar_signal = true;
									}else{
										event.getComponent().removeStyleName("valid-cpf");
										event.getComponent().addStyleName("invalid-cpf");
										Validar_signal = false;	
									}
								}
							}
					});	
				}
		});	
		
		vlRoot.addComponent(new FormLayout(){
			{
				setStyleName("form-cutom");
				setMargin(true);
				setSpacing(true);
				
				txtSinalDb = (TextField)fieldGroup.buildAndBind("Sinal Db", "sinal_db");	
				txtSinalDb.setId("txtSinalDb");
				txtSinalDb.setWidth("60px");				
				txtSinalDb.setStyleName("caption-align-contrato");
				txtSinalDb.setNullRepresentation("");					
				txtSinalDb.setRequired(true);
				txtSinalDb.setMaxLength(5);
				
				addComponent(txtSinalDb);
				JavaScript.getCurrent().execute("$('#txtSinalDb').maskMoney({decimal:'.'})");
				
				txtSinalDb.setEnabled(true);
				txtSinalDb.setRequired(true);
			
				Label lb = new Label("-");
				lb.setWidth("1%");
				lb.setStyleName("lbMinusCaixaAtendimento");
				
				addComponent(lb); 
			}
		});
		
		
	}
	
	TextField txtGpon;
	TextField txtSinalDb;
	TextField txtPON;
	TextField txtOLT;
	
	private JPAContainer<Swith> biuldContainerSwith(){
		JPAContainer<Swith> swithsContainer = JPAContainerFactory.make(Swith.class, ConnUtil.getEntity());
		//swithsContainer.addContainerFilter(Filters.eq("concentrador", new Concentrador(codConcentrador)));
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
		
		
		
		if(fieldGroup.isValid() && Validar_signal){
			try {												
				fieldGroup.commit();
				
				if(tipo_concentrador_selecionado.equals("mikrotik")){
					//item.getItemProperty("interfaces").setValue(cbInterface.getValue().toString());
				}

				if(fieldGroup.getField("signal_strength").getValue() == null || fieldGroup.getField("signal_strength").getValue().equals("")){
					item.getItemProperty("signal_strength").setValue("-120..120");
					Validar_signal = true;
				}
				
				if(!cbCto.isEnabled()){
					item.getItemProperty("swith").setValue(null);
				}
					
				fireEvent(new AlterarCtoEvent(getUI(), item, true));	
				close();
				
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
					fireEvent(new AlterarCtoEvent(getUI(), item, false));
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
	
	
	public void addListerner(AlterarCtoListerner target){
		try {
			Method method = AlterarCtoListerner.class.getDeclaredMethod("onClose", AlterarCtoEvent.class);
			addListener(AlterarCtoEvent.class, target, method);
		} catch (Exception e) {
			System.out.println("Método não Encontrado!");
		}
	}
	public void removeListerner(AlterarCtoListerner target){
		removeListener(AlterarCtoEvent.class, target);
	}
	public static class AlterarCtoEvent extends Event{
		
		private Item item;
		private boolean confirm;		
		
		public AlterarCtoEvent(Component source, Item item, boolean confirm) {
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
	public interface AlterarCtoListerner extends Serializable{
		public void onClose(AlterarCtoEvent event);
	}
}