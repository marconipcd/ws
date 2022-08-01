package BERG;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import domain.AcessoCliente;
import domain.Concentrador;

public class alterarConcentradores {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("OpusBloqueio");
	public static void main(String[] args) {
		
		List<String> contratos = new ArrayList<String>();
		contratos.add("16");
		contratos.add("31");
		contratos.add("107");
		contratos.add("133");
		contratos.add("197");
		contratos.add("204");
		contratos.add("246");
		contratos.add("318");
		contratos.add("329");
		contratos.add("372");
		contratos.add("532");
		contratos.add("557");
		contratos.add("723");
		contratos.add("752");
		contratos.add("796");
		contratos.add("965");
		contratos.add("981");
		contratos.add("999");
		contratos.add("1009");
		contratos.add("1087");
		contratos.add("1104");
		contratos.add("1188");
		contratos.add("1191");
		contratos.add("1230");
		contratos.add("1273");
		contratos.add("1334");
		contratos.add("1345");
		contratos.add("1369");
		contratos.add("1406");
		contratos.add("1459");
		contratos.add("1461");
		contratos.add("1491");
		contratos.add("1494");
		contratos.add("1516");
		contratos.add("1584");
		contratos.add("1645");
		contratos.add("1702");
		contratos.add("1706");
		contratos.add("1793");
		contratos.add("1820");
		contratos.add("2030");
		contratos.add("2088");
		contratos.add("2259");
		contratos.add("2300");
		contratos.add("2359");
		contratos.add("2378");
		contratos.add("2394");
		contratos.add("2454");
		contratos.add("2462");
		contratos.add("2501");
		contratos.add("2561");
		contratos.add("2630");
		contratos.add("2656");
		contratos.add("2698");
		contratos.add("2783");
		contratos.add("2844");
		contratos.add("2848");
		contratos.add("2857");
		contratos.add("2890");
		contratos.add("2938");
		contratos.add("2996");
		contratos.add("3136");
		contratos.add("3182");
		contratos.add("3192");
		contratos.add("3200");
		contratos.add("3249");
		contratos.add("3368");
		contratos.add("3370");
		contratos.add("3384");
		contratos.add("3410");
		contratos.add("3497");
		contratos.add("3507");
		contratos.add("3632");
		contratos.add("3641");
		contratos.add("3649");
		contratos.add("3811");
		contratos.add("3835");
		contratos.add("3837");
		contratos.add("3847");
		contratos.add("3878");
		contratos.add("3881");
		contratos.add("3908");
		contratos.add("3962");
		contratos.add("3991");
		contratos.add("3993");
		contratos.add("4025");
		contratos.add("4056");
		contratos.add("4064");
		contratos.add("4096");
		contratos.add("4167");
		contratos.add("4203");
		contratos.add("4214");
		contratos.add("4244");
		contratos.add("4246");
		contratos.add("4256");
		contratos.add("4278");
		contratos.add("4312");
		contratos.add("4315");
		contratos.add("4319");
		contratos.add("4356");
		contratos.add("4363");
		contratos.add("4367");
		contratos.add("4379");
		contratos.add("4381");
		contratos.add("4403");
		contratos.add("4428");
		contratos.add("4457");
		contratos.add("4476");
		contratos.add("4477");
		contratos.add("4525");
		contratos.add("4558");
		contratos.add("4599");
		contratos.add("4617");
		contratos.add("4627");
		contratos.add("4632");
		contratos.add("4639");
		contratos.add("4670");
		contratos.add("4674");
		contratos.add("4675");
		contratos.add("4717");
		contratos.add("4745");
		contratos.add("4772");
		contratos.add("4823");
		contratos.add("4825");
		contratos.add("4848");
		contratos.add("4857");
		contratos.add("4969");
		contratos.add("5003");
		contratos.add("5021");
		contratos.add("5061");
		contratos.add("5064");
		contratos.add("5119");
		contratos.add("5143");
		contratos.add("5164");
		contratos.add("5271");
		contratos.add("5295");
		contratos.add("5356");
		contratos.add("5372");
		contratos.add("5379");
		contratos.add("5405");
		contratos.add("5408");
		contratos.add("5420");
		contratos.add("5456");
		contratos.add("5458");
		contratos.add("5460");
		contratos.add("5465");
		contratos.add("5473");
		contratos.add("5482");
		contratos.add("5483");
		contratos.add("5528");
		contratos.add("5537");
		contratos.add("5543");
		contratos.add("5549");
		contratos.add("5558");
		contratos.add("5568");
		contratos.add("5580");
		contratos.add("5589");
		contratos.add("5621");
		contratos.add("5649");
		contratos.add("5664");
		contratos.add("5714");
		contratos.add("5724");
		contratos.add("5726");
		contratos.add("5729");
		contratos.add("5734");
		contratos.add("5805");
		contratos.add("5810");
		contratos.add("5828");
		contratos.add("5842");
		contratos.add("5861");
		contratos.add("5881");
		contratos.add("5931");
		contratos.add("5937");
		contratos.add("5947");
		contratos.add("5958");
		contratos.add("5972");
		contratos.add("5985");
		contratos.add("6002");
		contratos.add("6019");
		contratos.add("6042");
		contratos.add("6058");
		contratos.add("6059");
		contratos.add("6084");
		contratos.add("6146");
		contratos.add("6165");
		contratos.add("6166");
		contratos.add("6167");
		contratos.add("6176");
		contratos.add("6201");
		contratos.add("6211");
		contratos.add("6223");
		contratos.add("6311");
		contratos.add("6315");
		contratos.add("6344");
		contratos.add("6348");
		contratos.add("6366");
		contratos.add("6392");
		contratos.add("6395");
		contratos.add("6396");
		contratos.add("6426");
		contratos.add("6442");
		contratos.add("6474");
		contratos.add("6586");
		contratos.add("6595");
		contratos.add("6634");
		contratos.add("6647");
		contratos.add("6684");
		contratos.add("6738");
		contratos.add("6753");
		contratos.add("6774");
		contratos.add("6776");
		contratos.add("6894");
		contratos.add("6904");
		contratos.add("6908");
		contratos.add("6959");
		contratos.add("6974");
		contratos.add("6990");
		contratos.add("6998");
		contratos.add("6999");
		contratos.add("7002");
		contratos.add("7009");
		contratos.add("7040");
		contratos.add("7109");
		contratos.add("7123");
		contratos.add("7137");
		contratos.add("7156");
		contratos.add("7157");
		contratos.add("7177");
		contratos.add("7190");
		contratos.add("7213");
		contratos.add("7226");
		contratos.add("7233");
		contratos.add("7246");
		contratos.add("7260");
		contratos.add("7268");
		contratos.add("7269");
		contratos.add("7280");
		contratos.add("7289");
		contratos.add("7293");
		contratos.add("7304");
		contratos.add("7338");
		contratos.add("7340");
		contratos.add("7360");
		contratos.add("7368");
		contratos.add("7382");
		contratos.add("7435");
		contratos.add("7443");
		contratos.add("7464");
		contratos.add("7466");
		contratos.add("7490");
		contratos.add("7556");
		contratos.add("7572");
		contratos.add("7605");
		contratos.add("7638");
		contratos.add("7650");
		contratos.add("7685");
		contratos.add("7731");
		contratos.add("7751");
		contratos.add("7753");
		contratos.add("7820");
		contratos.add("7841");
		contratos.add("7842");
		contratos.add("7846");
		contratos.add("7890");
		contratos.add("7903");
		contratos.add("7933");
		contratos.add("7941");
		contratos.add("7960");
		contratos.add("7979");
		contratos.add("7999");
		contratos.add("8023");
		contratos.add("8030");
		contratos.add("8070");
		contratos.add("8108");
		contratos.add("8193");
		contratos.add("8230");
		contratos.add("8261");
		contratos.add("8267");
		contratos.add("8326");
		contratos.add("8363");
		contratos.add("8373");
		contratos.add("8384");
		contratos.add("8387");
		contratos.add("8422");
		contratos.add("8458");
		contratos.add("8470");
		contratos.add("8473");
		contratos.add("8483");
		contratos.add("8494");
		contratos.add("8534");
		contratos.add("8543");
		contratos.add("8589");
		contratos.add("8690");
		contratos.add("8716");
		contratos.add("8731");
		contratos.add("8738");
		contratos.add("8745");
		contratos.add("8775");
		contratos.add("8801");
		contratos.add("8805");
		contratos.add("8818");
		contratos.add("8822");
		contratos.add("8884");
		contratos.add("8915");
		contratos.add("8931");
		contratos.add("8950");
		contratos.add("8998");
		contratos.add("9041");
		contratos.add("9048");
		contratos.add("9120");
		contratos.add("9124");
		contratos.add("9139");
		contratos.add("9153");
		contratos.add("9201");
		contratos.add("9205");
		contratos.add("9221");
		contratos.add("9246");
		contratos.add("9264");
		contratos.add("9265");
		contratos.add("9288");
		contratos.add("9289");
		contratos.add("9308");
		contratos.add("9326");
		contratos.add("9336");
		contratos.add("9382");
		contratos.add("9383");
		contratos.add("9398");
		contratos.add("9401");
		contratos.add("9423");
		contratos.add("9426");
		contratos.add("9482");
		contratos.add("9483");
		contratos.add("9504");
		contratos.add("9523");
		contratos.add("9526");
		contratos.add("9545");
		contratos.add("9589");
		contratos.add("9594");
		contratos.add("9606");
		contratos.add("9612");
		contratos.add("9628");
		contratos.add("9673");
		contratos.add("9677");
		contratos.add("9681");
		contratos.add("9708");
		contratos.add("9761");
		contratos.add("9769");
		contratos.add("9773");
		contratos.add("9780");
		contratos.add("9786");
		contratos.add("9866");
		contratos.add("9880");
		contratos.add("9909");
		contratos.add("9963");
		contratos.add("9967");
		contratos.add("10021");
		contratos.add("10024");
		contratos.add("10026");
		contratos.add("10038");
		contratos.add("10065");
		contratos.add("10069");
		contratos.add("10076");
		contratos.add("10082");
		contratos.add("10109");
		contratos.add("10113");
		contratos.add("10117");
		contratos.add("10131");
		contratos.add("10156");
		contratos.add("10157");
		contratos.add("10169");
		contratos.add("10213");
		contratos.add("10220");
		contratos.add("10237");
		contratos.add("10241");
		contratos.add("10242");
		contratos.add("10254");
		contratos.add("10272");
		contratos.add("10275");
		contratos.add("10306");
		contratos.add("10325");
		contratos.add("10339");
		contratos.add("10342");
		contratos.add("10354");
		contratos.add("10369");
		contratos.add("10373");
		contratos.add("10380");
		contratos.add("10411");
		contratos.add("10428");
		contratos.add("10458");
		contratos.add("10477");
		contratos.add("10490");
		contratos.add("10512");
		contratos.add("10569");
		contratos.add("10579");
		contratos.add("10607");
		contratos.add("10646");
		contratos.add("10657");
		contratos.add("10659");
		contratos.add("10701");
		contratos.add("10704");
		contratos.add("10707");
		contratos.add("10720");
		contratos.add("10744");
		contratos.add("10752");
		contratos.add("10755");
		contratos.add("10757");
		contratos.add("10765");
		contratos.add("10801");
		contratos.add("10824");
		contratos.add("10848");
		contratos.add("10868");
		contratos.add("10872");
		contratos.add("10877");
		contratos.add("10909");
		contratos.add("10933");
		contratos.add("10951");
		contratos.add("10966");
		contratos.add("10976");
		contratos.add("10991");
		contratos.add("11016");
		contratos.add("11032");
		contratos.add("11052");
		contratos.add("11076");
		contratos.add("11081");
		contratos.add("11082");
		contratos.add("11089");
		contratos.add("11090");
		contratos.add("11094");
		contratos.add("11099");
		contratos.add("11118");
		contratos.add("11128");
		contratos.add("11134");
		contratos.add("11141");
		contratos.add("11151");
		contratos.add("11154");
		contratos.add("11175");
		contratos.add("11200");
		contratos.add("11232");
		contratos.add("11246");
		contratos.add("11253");
		contratos.add("11291");
		contratos.add("11337");
		contratos.add("11347");
		contratos.add("11394");
		contratos.add("11405");
		contratos.add("11415");
		contratos.add("11418");
		contratos.add("11419");
		contratos.add("11441");
		contratos.add("11450");
		contratos.add("11467");
		contratos.add("11468");
		contratos.add("11497");
		contratos.add("11502");
		contratos.add("11503");
		contratos.add("11506");
		contratos.add("11510");
		contratos.add("11516");
		contratos.add("11523");
		contratos.add("11543");
		contratos.add("11548");
		contratos.add("11549");
		contratos.add("11559");
		contratos.add("11578");
		contratos.add("11591");
		contratos.add("11598");
		contratos.add("11612");
		contratos.add("11638");
		contratos.add("11644");
		contratos.add("11664");
		contratos.add("11679");
		contratos.add("11682");
		contratos.add("11705");
		contratos.add("11714");
		contratos.add("11720");
		contratos.add("11731");
		contratos.add("11745");
		contratos.add("11753");
		contratos.add("11756");
		contratos.add("11757");
		contratos.add("11761");
		contratos.add("11762");
		contratos.add("11805");
		contratos.add("11832");
		contratos.add("11868");
		contratos.add("11894");
		contratos.add("11910");
		contratos.add("11968");
		contratos.add("11975");
		contratos.add("11976");
		contratos.add("11996");
		contratos.add("11998");
		contratos.add("12034");
		contratos.add("12037");
		contratos.add("12050");
		contratos.add("12059");
		contratos.add("12063");
		contratos.add("12090");
		contratos.add("12102");
		contratos.add("12105");
		contratos.add("12109");
		contratos.add("12152");
		contratos.add("12161");
		contratos.add("12164");
		contratos.add("12167");
		contratos.add("12192");
		contratos.add("12238");
		contratos.add("12249");
		contratos.add("12252");
		contratos.add("12282");
		contratos.add("12317");
		contratos.add("12365");
		contratos.add("12368");
		contratos.add("12382");
		contratos.add("12398");
		contratos.add("12401");
		contratos.add("12408");
		contratos.add("12417");
		contratos.add("12428");
		contratos.add("12437");
		contratos.add("12469");
		contratos.add("12473");
		contratos.add("12475");
		contratos.add("12485");
		contratos.add("12497");
		contratos.add("12505");
		contratos.add("12517");
		contratos.add("12543");
		contratos.add("12559");
		contratos.add("12570");
		contratos.add("12595");
		contratos.add("12597");
		contratos.add("12607");
		contratos.add("12609");
		contratos.add("12610");



		EntityManager em = emf.createEntityManager();
		Concentrador base = em.find(Concentrador.class, 149);
		
		for (String string : contratos) {
			try{
					AcessoCliente contrato = em.find(AcessoCliente.class, Integer.parseInt(string));
					
					em.getTransaction().begin();
					
						contrato.setBase(base);			
						em.merge(contrato);
						System.out.println(contrato.getId().toString());
						
					em.getTransaction().commit();
			
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		
	}
	

	

//contratos alterados para concentrador 92	
//	4055
//	4222
//	5647
//	6046
//	6637
//	7718
//	9631
//	5580
//	6904
//	7751
//	7890
//	8267
//	8373
//	8422
//	7990
//	2602
//	6725
//	6802
//	6914
//	8147
//	6348
//	6908
//	2403
//	4409
//	6231
//	6428
//	7954
//	8192
//	8345
//	9391
//	738
//	3693
//	4948
//	6020
//	6427
//	3391
//	5649
//	7504
//	7999
//	8738
//	9423
//	9606
//	9786
//	9899
//	447
//	836
//	996
//	2510
//	5240
//	5297
//	6949
//	7259
//	7399
//	8487
//	8551
//	8903
//	9098
//	3384
//	5734
//	6059
//	7368
//	8282
//	9055
//	9221
//	1620
//	3036
//	4503
//	4715
//	6450
//	7065
//	7599
//	8615
//	8956
//	9736
//	2600
//	5712
//	6755
//	6960
//	7187
//	7410
//	7721
//	8186
//	9927
//	821
//	3575
//	5444
//	9931
//	3249
//	6643
//	6959
//	7382
//	9398
//	6974
//	2469
//	3068
//	8850
//	8883
//	9359
//	164
//	1540
//	1793
//	2359
//	4457
//	5972
//	6999
//	7040
//	9584
//	16
//	2438
//	3182
//	3847
//	4632
//	5842
//	5881
//	7650
//	7676
//	8915
//	6990
//	9482
//	9750
//	9978
//	557
//	1702
//	2783
//	3192
//	4244
//	5947
//	6998
//	7459
//	8766
//	9307
//	9909
//	9963
//	318
//	3991
//	3993
//	4476
//	5064
//	6084
//	6201
//	6426
//	7009
//	7157
//	7304
//	7340
//	8009
//	8108
//	8470
//	303
//	5496
//	6182
//	6996
//	7228
//	8841
//	9766
//	4141
//	4878
//	7995
//	8951
//	9998
//	3614
//	6586
//	7002
//	7979
//	8884
//	8950
//	9594
//	3835
//	8070
//	160
//	1635
//	3776
//	4041
//	4867
//	5049
//	5103
//	5662
//	6076
//	6761
//	7160
//	8572
//	9560
//	9810
//	503
//	3561
//	5518
//	7031
//	8005
//	8040
//	8117
//	8284
//	8754
//	8905
//	2279
//	6852
//	6856
//	7049
//	7168
//	7761
//	9213
//	9214
//	9715
//	7070
//	7853
//	7973
//	8790
//	9243
//	1461
//	5714
//	6147
//	6311
//	9962
//	6378
//	6514
//	6819
//	7336
//	8241
//	8271
//	8288
//	8812
//	9455
//	9749
//	7231
//	8580
//	8600
//	8919
//	8975
//	8976
//	4675
//	539
//	2530
//	3956
//	5527
//	7100
//	7444
//	7491
//	7558
//	8891
//	9616
//	9883
//	9884
//	5164
//	7631
//	7892
//	9483
//	9523
//	329
//	2561
//	2630
//	4354
//	5061
//	5535
//	6042
//	6392
//	7464
//	8363
//	8543
//	9673
//	9880
//	3410
//	4063
//	5482
//	6395
//	7123
//	7443
//	8494
//	8679
//	133
//	1369
//	3136
//	4096
//	4627
//	7120
//	7280
//	8096
//	9120
//	9124
//	9945
//	1433
//	4278
//	5664
//	6366
//	7269
//	9526
//	136
//	4775
//	7138
//	7282
//	7310
//	7697
//	8579
//	8946
//	2996
//	5271
//	7137
//	7688
//	7846
//	9264
//	1459
//	7156
//	7233
//	7293
//	7556
//	7841
//	8246
//	9589
//	3174
//	4745
//	4772
//	8458
//	209
//	6488
//	8141
//	9650
//	9992
//	2953
//	4273
//	4376
//	4568
//	4771
//	6712
//	6995
//	8221
//	8325
//	8714
//	9067
//	9785
//	2088
//	7169
//	9336
//	36
//	4118
//	5742
//	5914
//	7958
//	8069
//	2800
//	5336
//	7183
//	7391
//	7863
//	8464
//	9437
//	1623
//	4639
//	5483
//	5958
//	7466
//	9295
//	9426
//	9708
//	826
//	5021
//	5295
//	8473
//	9692
//	9773
//	239
//	3579
//	7197
//	7294
//	3878
//	7190
//	5408
//	6019
//	7213
//	7490
//	4670
//	5372
//	5402
//	6146
//	7842
//	7960
//	8193
//	9943
//	4025
//	4825
//	5689
//	6315
//	7226
//	7562
//	7638
//	9205
//	9845
//	752
//	4363
//	5918
//	6401
//	8731
//	8866
//	9769
//	1188
//	4085
//	4428
//	5458
//	5861
//	7289
//	7605
//	8775
//	679
//	2848
//	4403
//	4717
//	5621
//	6753
//	538
//	7342
//	9551
//	3962
//	7246
//	7572
//	9442
//	5805
//	7260
//	7268
//	4035
//	4874
//	5517
//	6185
//	6244
//	6543
//	6571
//	8759
//	8784
//	9021
//	9951
//	4073
//	7287
//	8595
//	8826
//	1191
//	4674
//	8998
//	734
//	981
//	1584
//	1820
//	4167
//	5119
//	5379
//	6223
//	8805
//	2890
//	3641
//	4054
//	4246
//	5405
//	8483
//	8690
//	9201
//	2874
//	5750
//	7326
//	9440
//	965
//	6442
//	7338
//	7755
//	8257
//	2454
//	5143
//	5473
//	7360
//	7932
//	8057
//	8717
//	9326
//	4364
//	5435
//	5505
//	6213
//	6505
//	6707
//	7468
//	8713
//	8820
//	9061
//	9447
//	9622
//	9871
//	9993
//	204
//	2844
//	4823
//	5537
//	6417
//	6738
//	7852
//	8023
//	8716
//	8818
//	9628
//	1334
//	4056
//	5420
//	5659
//	6396
//	7435
//	7941
//	9362
//	9970
//	1104
//	1230
//	5729
//	5810
//	7719
//	9753
//	4214
//	9041
//	9677
//	7445
//	7479
//	8701
//	9497
//	31
//	1645
//	2300
//	3632
//	3881
//	4379
//	4967
//	6211
//	7731
//	8745
//	9139
//	9566
//	9924
//	480
//	6448
//	6687
//	8322
//	535
//	3682
//	6800
//	8781
//	9411
//	9911
//	1061
//	3031
//	3669
//	4994
//	8686
//	9428
//	9671
//	9784
//	9910
//	107
//	5931
//	7132
//	776
//	4861
//	5791
//	6878
//	7373
//	7511
//	9181
//	9344
//	4857
//	5562
//	8477
//	4570
//	9177
//	3200
//	9246
//	9265
//	9405
//	2501
//	7177
//	7753
//	9722
//	3507
//	7784
//	8589
//	8593
//	8822
//	9596
//	9712
//	7685
//	9779
//	532
//	1273
//	3064
//	5558
//	5770
//	999
//	2641
//	4367
//	5456
//	6002
//	6045
//	8499
//	9048
//	9612
//	5460
//	7580
//	7820
//	8384
//	8391
//	5927
//	6120
//	8375
//	9051
//	9570
//	9892
//	372
//	3370
//	4324
//	5549
//	8457
//	6719
//	8756
//	9024
//	3563
//	8176
//	8871
//	9153
//	9780
//	8634
//	9128
//	1516
//	4203
//	7903
//	7933
//	9251
//	9643
//	4064
//	5724
//	9629
//	9824
//	9898
//	4477
//	5543
//	6166
//	8261
//	8326
//	8030
//	8931
//	3560
//	4525
//	8387
//	8864
//	9670
//	2794
//	3429
//	4111
//	8643
//	8892
//	9646
//	9815
//	9913
//	1375
//	3559
//	4078
//	4323
//	5162
//	8834
//	796
//	8230
//	4868
//	5820
//	9439
//	9449
//	197
//	2974
//	5528
//	9288
//	9308
//	8534
//	9401
//	502
//	4900
//	8557
//	9253
//	9500
//	8801
//	726
//	9382
//	9383
//	9525
//	4112
	
	
	//contratos alterados para concentrador 36
//	4098
//	6040
//	6873
//	7777
//	8934
//	8952
//	8035
//	9668
//	9819
//	579
//	1064
//	1643
//	5499
//	6207
//	6982
//	7066
//	7506
//	8183
//	9057
//	9168
//	9402
//	9569
//	5671
//	8673
//	9321
//	7369
//	8161
//	8724
//	8845
//	9242
//	9687
//	57
//	4858
//	6414
//	7015
//	9035
//	9125
//	9171
//	9387
//	431
//	469
//	2739
//	7914
//	5040
//	6969
//	7036
//	7125
//	7860
//	9272
//	9273
//	9844
//	725
//	4286
//	5964
//	7062
//	7873
//	8385
//	8699
//	8959
//	9089
//	9132
//	9271
//	9274
//	2503
//	3332
//	380
//	2476
//	3816
//	4813
//	6942
//	7057
//	8152
//	9820
//	4749
//	7108
//	8112
//	8722
//	582
//	7600
//	8039
//	8406
//	9074
//	9155
//	9179
//	9425
//	9742
//	9947
//	4142
//	4533
//	5252
//	7307
//	8862
//	8973
//	9672
//	2043
//	6500
//	6694
//	7441
//	7857
//	9353
//	3689
//	6497
//	7232
//	9172
//	2549
//	4248
//	6988
//	7039
//	7316
//	7645
//	8744
//	79
//	99
//	1147
//	2394
//	3488
//	5144
//	6091
//	7001
//	7762
//	8742
//	9316
//	80
//	499
//	2955
//	3611
//	4722
//	5785
//	6025
//	6413
//	6585
//	9358
//	9853
//	5690
//	6994
//	7099
//	9160
//	9854
//	7020
//	8408
//	2751
//	3498
//	4945
//	5311
//	6353
//	6525
//	7076
//	7167
//	8832
//	9432
//	9800
//	527
//	2617
//	4215
//	4936
//	5921
//	7180
//	7785
//	8340
//	8778
//	29
//	2283
//	2956
//	4058
//	7398
//	7518
//	7705
//	7908
//	8013
//	8746
//	2776
//	6857
//	9257
//	2010
//	2875
//	8015
//	8460
//	9254
//	9676
//	4155
//	7014
//	7681
//	7802
//	7982
//	6675
//	7482
//	8984
//	482
//	3157
//	3223
//	4401
//	6195
//	7043
//	7178
//	7591
//	8836
//	9663
//	3524
//	5281
//	5915
//	8621
//	717
//	2212
//	3387
//	4979
//	5247
//	7716
//	8089
//	8782
//	9458
//	607
//	3422
//	4352
//	4909
//	6328
//	2724
//	5461
//	6502
//	7902
//	8869
//	117
//	366
//	638
//	1151
//	5123
//	8644
//	9335
//	9
//	53
//	3397
//	3932
//	6391
//	7655
//	9317
//	1122
//	2626
//	4282
//	5282
//	6088
//	7086
//	8397
//	8474
//	8875
//	9406
//	9543
//	406
//	3876
//	4052
//	6662
//	7311
//	7330
//	8914
//	9501
//	9874
//	590
//	3729
//	5077
//	5407
//	7082
//	8332
//	8939
//	9759
//	490
//	4406
//	4833
//	6713
//	6948
//	7093
//	9609
//	337
//	2663
//	3061
//	8848
//	9196
//	10004
//	496
//	894
//	1553
//	2002
//	4788
//	5453
//	7175
//	7944
//	9588
//	9835
//	9891
//	7103
//	7783
//	8376
//	8720
//	9113
//	9600
//	9607
//	9679
//	782
//	2937
//	9312
//	9429
//	163
//	7042
//	7299
//	8763
//	9796
//	4985
//	5782
//	9133
//	9794
//	9901
//	38
//	3020
//	3294
//	3300
//	3934
//	9748
//	4102
//	4452
//	7619
//	8356
//	9375
//	9714
//	9935
//	114
//	3959
//	7152
//	7498
//	8165
//	8353
//	9902
//	858
//	2235
//	3266
//	3851
//	4031
//	7321
//	8435
//	8890
//	23
//	578
//	1883
//	2686
//	3975
//	4752
//	5474
//	8033
//	8922
//	8937
//	9068
//	9586
//	3372
//	5284
//	6681
//	8004
//	8021
//	8437
//	10018
//	4983
//	7172
//	9509
//	3854
//	4733
//	5261
//	5886
//	248
//	824
//	2707
//	3057
//	4758
//	7274
//	7826
//	7851
//	8049
//	9103
//	9154
//	9653
//	2969
//	3779
//	4318
//	7129
//	7224
//	7573
//	8109
//	9534
//	407
//	2859
//	3425
//	6067
//	7188
//	7495
//	8488
//	971
//	7186
//	8274
//	1364
//	5713
//	5776
//	6834
//	7215
//	7797
//	7988
//	2347
//	5414
//	6930
//	8872
//	9012
//	4132
//	4436
//	2673
//	8148
//	8549
//	9690
//	2603
//	2873
//	2990
//	7253
//	7678
//	8067
//	8179
//	8182
//	9173
//	9555
//	276
//	4560
//	4963
//	6003
//	7904
//	8696
//	3272
//	5232
//	5818
//	6546
//	9919
//	4301
//	5462
//	7796
//	7849
//	8334
//	8993
//	9481
//	2232
//	4329
//	89
//	2967
//	8341
//	9079
//	9403
//	9669
//	9772
//	3355
//	4886
//	9370
//	9377
//	9747
//	1370
//	5010
//	6579
//	7302
//	8006
//	8029
//	138
//	440
//	7315
//	7976
//	9038
//	1089
//	2862
//	4798
//	7416
//	8941
//	208
//	1097
//	2436
//	2768
//	4377
//	4740
//	7319
//	7585
//	9590
//	9657
//	9955
//	10009
//	5074
//	7452
//	8270
//	8416
//	9454
//	9531
//	207
//	3915
//	7765
//	3307
//	4879
//	6600
//	6759
//	7474
//	8453
//	8537
//	9302
//	3509
//	9262
//	9778
//	168
//	7985
//	8111
//	8124
//	8227
//	9237
//	326
//	415
//	5353
//	5442
//	5699
//	5896
//	7492
//	8255
//	9037
//	9514
//	9521
//	1538
//	4795
//	12
//	7384
//	8153
//	8779
//	542
//	2437
//	3773
//	8259
//	789
//	1136
//	1774
//	3127
//	3973
//	7828
//	8578
//	9487
//	13
//	3947
//	4493
//	6671
//	7680
//	9513
//	352
//	1221
//	4120
//	5401
//	9148
//	9655
//	9788
//	436
//	7669
//	8847
//	9104
//	9635
//	4635
//	7471
//	7774
//	9857
//	9918
//	9940
//	10011
//	3428
//	8200
//	8401
//	8613
//	9270
//	386
//	2741
//	3989
//	4125
//	4934
//	5030
//	5715
//	5833
//	6007
//	7085
//	7690
//	8016
//	8480
//	3214
//	7617
//	7963
//	9693
//	533
//	866
//	4546
//	8219
//	8535
//	9238
//	9431
//	7637
//	7816
//	1327
//	6654
//	9056
//	127
//	4393
//	5801
//	6459
//	7651
//	7987
//	8601
//	9020
//	9328
//	9875
//	3357
//	4247
//	5256
//	9084
//	9351
//	1128
//	4671
//	6610
//	7675
//	7757
//	9713
//	627
//	8269
//	2365
//	8114
//	9863
//	898
//	8661
//	9152
//	399
//	589
//	1042
//	2891
//	4992
//	3505
//	5097
//	7929
//	8390
//	2828
//	8887
//	8091
//	2443
//	4029
//	5732
//	6343
//	6885
//	8081
//	9130
//	25
//	181
//	1060
//	3138
//	5700
//	8436
//	1274
//	6097
//	6329
//	8497
//	9258
//	9989
//	4143
//	4871
//	9042
//	9505
//	9592
//	9868
//	3004
//	5887
//	6126
//	8144
//	8210
//	8469
//	8565
//	8628
//	9066
//	6444
//	7290
//	8799
//	229
//	2422
//	2824
//	7386
//	8429
//	4937
//	8238
//	3984
//	8743
//	9394
//	8548
//	8936
//	8616
//	3311
//	8676
//	9058
//	9548
//	8803
//	8983
//	7053
//	9415
//	247
//	3999
//	7278
//	9577

}
