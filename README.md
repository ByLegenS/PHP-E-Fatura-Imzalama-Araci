# PHP ile GİB EFatura İmzalama

Özellikleri:
Java Kütüphanesi kullanarak GİB EFatura imzalar.PHP exec fonksiyonunun açık olması gerekmektedir.Bilgisayarda Java yüklü olması gerekmektedir.


Geliştirici: Yücel Kahraman (http://yucelkahraman.com.tr)
             https://github.com/3zRasasi/PHP-E-Fatura-Imzalama-Araci

## Kullanım
```php
	/* Smart Kart Fatura İmzalama */  
	function Imzala($java_dosyasi,$fatura_dosyasi,$pin=null,$seriImza=false,$dosyayaKaydet=true)  
	{  
	/* Java Dosyasına Veri Yollanıyor */  
	if($pin!=null)  { 
	exec("java -Dfile.encoding=UTF8 -jar ".$java_dosyasi." {".$fatura_dosyasi.",".$pin.",".$seriImza.",".$dosyayaKaydet."} 2>&1",$cikti);    
	$sonuc=$cikti;  

	print_r($cikti);
	
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
	
	
	/* Smart Kart Fatura İmzalama */   $dizin=(__DIR__ . DIRECTORY_SEPARATOR);  $java_dosyasi=$dizin."Java/NTGEFaturaImza.jar";  
	$fatura_dosyasi='TemelFaturaOrnegi.xml';  
	$pin='123456';   
	$cikti=Imzala($java_dosyasi,$fatura_dosyasi,$pin);   
	
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





