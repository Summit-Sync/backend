xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/price' name=asd price:=123
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/qualification' name='Erste Hilfe'
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/location' street="Gastfeldstr. 12" postcode="49312" title="Main address" city="My City"
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/trainer' username="createcouresh$(( ( RANDOM % 10 )  + 1 ))" password=1234 firstName=Lukas lastName=Müller email="createcouresh$(( ( RANDOM % 10 )  + 1 ))@sh.de" phone=12356236
echo 'creating course. Press something'
read
xh -a $(./get_token.sh) -A bearer 'http://localhost:8080/api/v1/course'\
  acronym=kv 'dates:=["2024-08-01T10:00:25.739Z", "2024-08-02T10:00:40.456Z", "2024-08-03T10:00:00.000Z"]'\
  description=kvBeschreibrung \
  duration:=40 location:=1 meetingPoint=kvTreffpunkt notes=test numberParticipants:=2 numberTrainers:=2 numberWaitlist:=2 \
  'prices:=[{"name": "TestPrice", "price": "123.4"}]' 'requiredQualifications:=[1]' title=kvTitle visible:=false

