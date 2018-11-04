<?php 
//Geliştirici: Yücel Kahraman (http://yucelkahraman.com.tr) https://github.com/3zRasasi/PHP-E-Fatura-Imzalama-Araci
//GPLv3 Genel Kamu Lisansı ile lisanslanmıştır. Detaylar için LİSANS dosyasına bakın.





    //Dosya ayarları
	$dizin=(__DIR__ . DIRECTORY_SEPARATOR);  
	$java_dosyasi=$dizin."Java\NTGEFaturaImza.jar";  
	$fatura_dosyasi=$dizin."TemelFaturaOrnegi.xml"; 
	
	//Akıllı Kart şifresi
	$pin='123456';  
	
	//Seri imza olsun mu?
	$seriImza="hayır";
	
    //Dosyaya kaydetsin mi?
	$dosyayaKaydet="evet";


	

	/* Smart Kart Fatura İmzalama */  
	function Imzala($java_dosyasi,$fatura_dosyasi,$pin=null,$seriImza,$dosyayaKaydet)  
	{  
	
		/* Java 0 - 1 i kabul etmiyor. İlla True - False olacak diyor */ 
	if ($seriImza== "hayır") { $seriImza= "false"; } elseif ($seriImza== "evet") { $seriImza= "true"; }
	if ($dosyayaKaydet== "hayır") { $dosyayaKaydet= "false"; } elseif ($dosyayaKaydet== "evet") { $dosyayaKaydet= "true"; }
	
	
		/* Java Dosyasına Veri Yollanıyor */  
		if($pin!=null)  
		{ 
				$java_komutu= $java_dosyasi." {".$fatura_dosyasi.",".$pin.",".$seriImza.",".$dosyayaKaydet."}";
				exec("java -Dfile.encoding=UTF8 -jar ".$java_komutu." 2>&1",$cikti);    
				$sonuc=$cikti;  

				
				foreach($cikti AS $ciktiDeger)  
				{  
	
						if(strstr($ciktiDeger,'İmzalandı...')==true)  
						{  
								$sonuc=true;  break;  
						}  
				}   
			return $sonuc;  
		}  
	}  
	
	
	
	
	if($cikti==true)
	{
		echo "İmzalandı..."; 
	}		
	else 
	{
		echo "Hata Oluştu...";
	}
	
?>
