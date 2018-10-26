# PHP ile GİB EFatura İmzalama

Özellikleri:

1)Algoritma ile TC Kimlik No ve Yabancı Kimlik No doğruluk kontrolü yapar,

2)Nüfus ve Vatandaşlık İşleri Genel Müdürlüğü ile 
-TC Kimlik No, 
-Yabancı Kimlik No,
-Nüfus Cüzdanı,
-Yeni Kimlik Kartı doğruluk kontrolü yapar,

3)PHP Curl ve Soap bağlantısını destekler,

4)PHP Curl ve Soap aktif değilse sadece Algoritma ile doğruluk kontrolü yapar.

Geliştirici: Murat KARAGÖZ (murat.karagoz@hotmail.com.tr)

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
Creative Commons Atıf-GayriTicari-Türetilemez 4.0 Uluslararası Kamu Lisansı ile lisanslanmıştır. Detaylar için LİSANS dosyasına bakın.  (Orjinali: https://github.com/3zRasasi/PHP-E-Fatura-Imzalama-Araci )





