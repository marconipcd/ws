package ADEMIR;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import domain.AcessoCliente;

public class cruzarDadosParamount {
	
	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	static EntityManager em = emf.createEntityManager();

	public static void main(String[] args){
				
		List<String> cpf= new ArrayList<>();
		cpf.add("06720001485");
		cpf.add("17126070497");
		cpf.add("13755632462");
		cpf.add("05984915464");
		cpf.add("04463795494");
		cpf.add("12006028464");
		cpf.add("70878145400");
		cpf.add("05244302418");
		cpf.add("04494589462");
		cpf.add("07413800489");
		cpf.add("01976811589");
		cpf.add("08769533997");
		cpf.add("11284383482");
		cpf.add("38747879823");
		cpf.add("06694468407");
		cpf.add("05050551471");
		cpf.add("10896571459");
		cpf.add("70270550402");
		cpf.add("10295859466");
		cpf.add("10098890441");
		cpf.add("43905951000112");
		cpf.add("70606168451");
		cpf.add("10277737451");
		cpf.add("09467135450");
		cpf.add("07382563405");
		cpf.add("14279995460");
		cpf.add("47649666468");
		cpf.add("02038848416");
		cpf.add("05568974470");
		cpf.add("09643037444");
		cpf.add("93503857400");
		cpf.add("09343390408");
		cpf.add("27501610886");
		cpf.add("10421339489");
		cpf.add("06694573456");
		cpf.add("05251919417");
		cpf.add("02956117408");
		cpf.add("08078847408");
		cpf.add("07843703896");
		cpf.add("07382568466");
		cpf.add("07491720455");
		cpf.add("04932944497");
		cpf.add("07524282419");
		cpf.add("09654578425");
		cpf.add("66856620420");
		cpf.add("06329606471");
		cpf.add("89985974468");
		cpf.add("10508580420");
		cpf.add("10508580420");
		cpf.add("12163897486");
		cpf.add("07239614417");
		cpf.add("02246302455");
		cpf.add("06673158450");
		cpf.add("13758113474");
		cpf.add("10123979471");
		cpf.add("13597557481");
		cpf.add("13187845495");
		cpf.add("06500408489");
		cpf.add("11318755409");
		cpf.add("06864375439");
		cpf.add("10881950408");
		cpf.add("68650779468");
		cpf.add("83332677420");
		cpf.add("07369088464");
		cpf.add("14121656440");cpf.add("09133094438");cpf.add("09376614496");
		cpf.add("12631522443");cpf.add("11207594474");cpf.add("72517174334");cpf.add("70754954102");
		cpf.add("32715625472");cpf.add("07946628544");cpf.add("10571405460");cpf.add("21225435404");
		cpf.add("12364514495");cpf.add("08515748401");cpf.add("11498357458");cpf.add("70512261423");
		cpf.add("14150610410");cpf.add("11953221432");cpf.add("14059832480");cpf.add("12260664458");
		cpf.add("05540346467");cpf.add("03256858430");cpf.add("10468941452");cpf.add("11407926446");
		cpf.add("09818072405");cpf.add("11826065474");cpf.add("08778963486");cpf.add("77202104415");
		cpf.add("49307258449");cpf.add("02985898420");cpf.add("04347335474");cpf.add("05752475414");
		cpf.add("08437996414");cpf.add("04214178467");cpf.add("51354616472");cpf.add("08067909466");
		cpf.add("05611556400");cpf.add("11873939400");cpf.add("06932844478");cpf.add("06728948406");
		cpf.add("02162762412");cpf.add("36616354415");cpf.add("05165390438");cpf.add("07468606412");
		cpf.add("09480809494");cpf.add("08443664479");cpf.add("59122943404");cpf.add("83608290478");
		cpf.add("71797173472");cpf.add("02938568428");cpf.add("09464957425");cpf.add("06844315481");
		cpf.add("10332633462");cpf.add("70372471463");cpf.add("99405660420");cpf.add("02581587431");
		cpf.add("65219201468");cpf.add("11485517435");cpf.add("02862333476");cpf.add("12040494405");
		cpf.add("09846873433");cpf.add("84946563415");cpf.add("02417496461");cpf.add("04971419497");
		cpf.add("07264227464");cpf.add("12833830475");cpf.add("03888105480");cpf.add("02188963440");
		cpf.add("02755981440");cpf.add("08248841405");cpf.add("06484071446");cpf.add("71863730460");
		cpf.add("10321086490");cpf.add("70227448405");cpf.add("11298373433");cpf.add("78373670491");
		cpf.add("06614606492");cpf.add("97240400468");cpf.add("04786838470");cpf.add("96384522420");
		cpf.add("12781382426");cpf.add("68868090406");cpf.add("11023941490");cpf.add("01837380473");
		cpf.add("09658102409");cpf.add("07237695401");cpf.add("07923083444");cpf.add("05406759469");
		cpf.add("01051638488");cpf.add("12659787433");cpf.add("14414182450");cpf.add("12136703498");
		cpf.add("84946830472");cpf.add("10323462480");cpf.add("11465910476");cpf.add("62445650453");
		cpf.add("70222351470");cpf.add("11013727452");cpf.add("83830596472");cpf.add("12176765460");
		cpf.add("06653428451");cpf.add("71024093204");cpf.add("10483986410");cpf.add("10993662404");
		cpf.add("86529471472");cpf.add("12526858402");cpf.add("10449565424");cpf.add("11355045479");
		cpf.add("13966401428");cpf.add("05186346470");cpf.add("12919114484");cpf.add("86907883449");
		cpf.add("10947315446");cpf.add("07531180413");cpf.add("66764335400");cpf.add("12919537440");
		cpf.add("07962474475");cpf.add("08566902440");cpf.add("07063947478");cpf.add("17740612448");
		cpf.add("11715962435");cpf.add("11378048407");cpf.add("14405492492");cpf.add("72043715429");
		cpf.add("94657904434");cpf.add("11858667445");cpf.add("12277597465");cpf.add("75514362434");
		cpf.add("03165826463");cpf.add("08047387421");cpf.add("04563307475");cpf.add("08279108432");
		cpf.add("71895215420");cpf.add("11402556497");cpf.add("14124329440");cpf.add("14408561410");
		cpf.add("70878633472");cpf.add("07236044440");cpf.add("04554261476");cpf.add("71044787449");
		cpf.add("07016202429");cpf.add("58591699491");cpf.add("12855606470");cpf.add("13142819410");
		cpf.add("08689581405");cpf.add("34273352400");cpf.add("11453337458");cpf.add("92276482400");
		cpf.add("12386921417");cpf.add("09393230447");cpf.add("12730278419");cpf.add("86566253653");
		cpf.add("07984152403");cpf.add("10166760420");cpf.add("70400447495");cpf.add("21624761852");
		cpf.add("70452993466");cpf.add("65792149453");cpf.add("00127336362");cpf.add("05872109407");
		cpf.add("05320911408");cpf.add("01398477400");cpf.add("09757588458");cpf.add("32631081404");
		cpf.add("12733166492");cpf.add("14398645438");cpf.add("07671798460");cpf.add("46110801453");
		cpf.add("96384581434");cpf.add("74988743420");cpf.add("02083079400");cpf.add("07803679447");
		cpf.add("66724244404");cpf.add("65219678434");cpf.add("16718158402");cpf.add("90061187453");
		cpf.add("02232174417");cpf.add("11994300485");cpf.add("05891918455");cpf.add("11624181414");
		cpf.add("50682350400");cpf.add("43529526487");cpf.add("34581405491");cpf.add("02993365495");
		cpf.add("41021738468");cpf.add("52747778487");cpf.add("18010768472");cpf.add("20751419400");
		cpf.add("06965021438");cpf.add("06725876455");cpf.add("01241301433");cpf.add("12250352445");
		cpf.add("07679620440");cpf.add("70990780465");cpf.add("14367694429");cpf.add("86183800406");
		cpf.add("10941018423");cpf.add("07335039428");cpf.add("43821251468");cpf.add("76976017453");
		cpf.add("84946458468");cpf.add("78374332468");cpf.add("09233528480");cpf.add("01443825476");
		cpf.add("06056294471");cpf.add("55496261449");cpf.add("05215602506");cpf.add("50878450459");
		cpf.add("49306570406");cpf.add("03302909403");cpf.add("04399569433");cpf.add("97240893472");
		cpf.add("07202704480");cpf.add("05601499489");cpf.add("98893793415");cpf.add("09653576437");
		cpf.add("02536530450");cpf.add("76422836449");cpf.add("07305962490");cpf.add("07077861422");
		cpf.add("07857374482");cpf.add("44822979415");cpf.add("13981612418");cpf.add("03800885417");
		cpf.add("08012532417");cpf.add("11407990454");cpf.add("75512289420");cpf.add("04605807446");
		cpf.add("13475111489");cpf.add("05570394443");cpf.add("05295376486");cpf.add("10498113485");
		cpf.add("11101777478");cpf.add("10441965482");cpf.add("55496547415");cpf.add("10829600477");
		cpf.add("12731062452");cpf.add("10567080420");cpf.add("10802915477");cpf.add("07276476448");
		cpf.add("12799745407");cpf.add("05251969430");cpf.add("07339757473");cpf.add("00982650400");
		cpf.add("06284823452");cpf.add("65345380406");cpf.add("07710351441");cpf.add("04198773475");
		cpf.add("02563702496");cpf.add("13075679402");cpf.add("07248703427");cpf.add("01735905275");
		cpf.add("47648481472");cpf.add("38647729404");cpf.add("41490304487");cpf.add("08123579497");
		cpf.add("11915383463");cpf.add("07995724462");cpf.add("41660754895");cpf.add("03410081410");
		cpf.add("65783964468");cpf.add("05568699402");cpf.add("04987479486");cpf.add("09613585486");
		cpf.add("57577455415");cpf.add("47688122449");cpf.add("58804595434");cpf.add("10719739403");
		cpf.add("10270013431");cpf.add("10398601410");cpf.add("06651655604");cpf.add("12592217410");
		cpf.add("08853028424");cpf.add("11169207430");cpf.add("12837710474");cpf.add("09400841477");
		cpf.add("13006702409");cpf.add("05540902440");cpf.add("70681602473");cpf.add("05301217437");
		cpf.add("30250716801");cpf.add("03283301409");cpf.add("00229396445");cpf.add("09842927482");
		cpf.add("10033529442");
		cpf.add("11461068401");
		cpf.add("10004299469");
		cpf.add("10462006409");cpf.add("04659241408");
		cpf.add("10945714408");
		cpf.add("03119243442");
		cpf.add("02889172430");
		cpf.add("07208051470");
		cpf.add("06106531480");
		cpf.add("06565491496");
		cpf.add("81868804453");
		cpf.add("40523794894");
		cpf.add("88226255449");
		cpf.add("66723930487");
		cpf.add("52102769291");
		cpf.add("04610749408");
		cpf.add("09892628470");
		cpf.add("10369787455");
		cpf.add("16173992485");
		cpf.add("03479832431");
		cpf.add("13715196416");
		cpf.add("08646280489");
		cpf.add("05244237411");
		cpf.add("85778303491");
		cpf.add("04152488417");
		cpf.add("12444924428");
		cpf.add("68249209400");
		cpf.add("12658182406");
		cpf.add("46196684434");
		cpf.add("09907716448");
		cpf.add("04967047459");
		cpf.add("10102168725");
		cpf.add("70901431478");
		cpf.add("03617903339");
		cpf.add("12226370498");
		cpf.add("07343046411");
		cpf.add("45980004491");
		cpf.add("08025165485");
		cpf.add("70895901439");
		cpf.add("08969456422");
		cpf.add("03457164428");
		cpf.add("01078334412");
		cpf.add("03880984492");
		cpf.add("07265516435");
		cpf.add("08763491494");
		cpf.add("04773089458");
		cpf.add("07138380437");
		cpf.add("10513787000149");
		cpf.add("02877179419");
		cpf.add("66654106849");
		cpf.add("10436924447");
		cpf.add("03952565423");
		cpf.add("04119500454");
		cpf.add("04715015730");
		cpf.add("05631178427");cpf.add("03802380479");
		cpf.add("71126651400");cpf.add("11913152405");
		cpf.add("12854907469");cpf.add("11753659477");
		cpf.add("05764857481");cpf.add("32427359866");
		cpf.add("02229723430");cpf.add("30154898449");
		cpf.add("70306107414");cpf.add("80907172415");
		cpf.add("68966997449");cpf.add("06636008474");
		cpf.add("03709224411");cpf.add("07181781499");
		cpf.add("29187370468");cpf.add("02191142427");
		cpf.add("05414668442");cpf.add("06346097423");
		cpf.add("13104113440");cpf.add("71547542497");
		cpf.add("09336072455");cpf.add("33153216800");
		cpf.add("12600094407");cpf.add("12854172400");
		cpf.add("01001342461");cpf.add("11066475482");
		cpf.add("11960445464");
		cpf.add("51192706897");
		cpf.add("12074296436");
		cpf.add("98970720472");
		cpf.add("11880850427");
		cpf.add("03109209497");
		cpf.add("03273843446");
		cpf.add("09649293418");
		cpf.add("03372013409");
		cpf.add("05177295478");
		
		
		StringBuilder sb = new StringBuilder();
		for (String c: cpf) {
			
			Query q = em.createQuery("select a from AcessoCliente a where a.cliente.doc_cpf_cnpj=:c and a.senha_paramount is not null", AcessoCliente.class);
			q.setParameter("c", c);
			
			
			List<AcessoCliente> contratos = q.getResultList();
			for (AcessoCliente contrato: contratos) {
				if(!contrato.getStatus_2().equals("ATIVO")){
					System.out.println(contrato.getCliente().getNome_razao()+","+c+","+contrato.getId().toString()+","+contrato.getStatus_2());
				}
			}
			
			if(q.getResultList().size() == 0){
				System.out.println("NAO ENCONTRADO: "+c);
			}
			
			
		}
		
		
		
			
		
		
		
	}

}
