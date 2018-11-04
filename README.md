# PHP ile GİB EFatura İmzalama

Özellikleri:
Java Kütüphanesi kullanarak GİB EFatura imzalar.PHP exec fonksiyonunun açık olması gerekmektedir.Bilgisayarda Java yüklü olması gerekmektedir.Mali Mühür Sertifikası gereklidir.


Geliştirici: Yücel Kahraman (http://yucelkahraman.com.tr)
             https://github.com/3zRasasi/PHP-E-Fatura-Imzalama-Araci

## Kullanım
```php

	/* Smart Kart Fatura İmzalama */   
	$dizin=(__DIR__ . DIRECTORY_SEPARATOR);  
	$java_dosyasi=$dizin."Java\NTGEFaturaImza.jar";  
	$fatura_dosyasi=$dizin."TemelFaturaOrnegi.xml";  
	$pin='123456';  
	
	

	
	
	/* Smart Kart Fatura İmzalama */  
	function Imzala($java_dosyasi,$fatura_dosyasi,$pin=null,$seriImza=false,$dosyayaKaydet=true)  
	{  
	
		/* Java 0 - 1 i kabul etmiyor. İlla True - False olacak diyor */ 
	if ($seriImza== 0) { $seriImza= "false"; } elseif ($seriImza== 1) { $seriImza= "true"; }
	if ($dosyayaKaydet== 0) { $dosyayaKaydet= "false"; } elseif ($dosyayaKaydet== 1) { $dosyayaKaydet= "true"; }
	
	
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
	
	
 
	$cikti=Imzala($java_dosyasi,$fatura_dosyasi,$pin,false,true);   
	
	if($cikti==true)
	{
		echo "İmzalandı..."; 
	}		
	else 
	{
		echo "Hata Oluştu...";
	}

 ``` 

### Lisans
GPLv3 Genel Kamu Lisansı ile lisanslanmıştır. Detaylar için LİSANS dosyasına bakın. 





