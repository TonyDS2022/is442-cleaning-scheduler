import requests
import numpy as np
import pandas as pd
import os
import re
from dotenv import load_dotenv
from typing import Tuple

# Constants
ENV_PATH = os.path.join("src", "main", "resources", ".env")
load_dotenv(ENV_PATH)
ONEMAP_API_URL = "https://www.onemap.gov.sg/"
ONEMAP_TOKEN = os.getenv("ONEMAP_TOKEN")
ONEMAP_ENDPOINT_SEARCH = "api/common/elastic/search"
ONEMAP_ENDPOINT_REVERSE_GEOCODE = "api/public/revgeocodexy"
GOOGLE_MAP_TOKEN = os.getenv("GOOGLE_OD_API_KEY")
GOOGLE_API_URL = "https://maps.googleapis.com/maps/"
GOOGLE_ENDPOINT_REVERSEGEOCODE = "api/geocode/json"

# Seed addresses
SEED_ADDRESSES = [
    {"postalCode": "438181", "address": "61 Kampong Arang Road"},
    {"postalCode": "649823", "address": "88 Corporation Road"},
    {"postalCode": "228051", "address": "28 Wilkie Raod"},
    {"postalCode": "188065", "address": "81 Victoria Street"},
    {"postalCode": "310480", "address": "480 Lorong 6 Toa Payoh"},
]

# Function to obtain latitude and longitude from address
def get_lat_long(searchVal: str) -> Tuple[float, float]:
    url = f"{ONEMAP_API_URL}{ONEMAP_ENDPOINT_SEARCH}?searchVal={searchVal}&returnGeom=Y&getAddrDetails=Y&pageNum=1"
    response = requests.get(url)
    data = response.json()
    
    if data["found"] == 0:
        return (0, 0)
    else:
        latitude = data["results"][0]["LATITUDE"]
        longitude = data["results"][0]["LONGITUDE"]
        return (latitude, longitude)

# Function to reverse geocode from latitude and longitude
def reverse_geocode(lat: float, long: float) -> Tuple[str, str]:
    BUFFER_METERS = 40
    url = f"{ONEMAP_API_URL}{ONEMAP_ENDPOINT_REVERSE_GEOCODE}?location={lat},{long}&buffer={BUFFER_METERS}&addressType=All&otherFeatures=N"
    header = {"Authorization": ONEMAP_TOKEN}
    response = requests.get(url, headers=header)
    
    if response.status_code == 200:
        data = response.json()
        try:
            locationJson = data["GeocodeInfo"][0]
        except IndexError:
            print(response.text)
            return ("", "")
        postalCode = locationJson["POSTALCODE"]
        address = f"{locationJson['BLOCK']} {locationJson['ROAD']}"
        return (postalCode, address)
    else:
        print(response.text)
        return ("", "")
    
def reverse_geocode_google(lat: float, long: float):
    url = f"{GOOGLE_API_URL}{GOOGLE_ENDPOINT_REVERSEGEOCODE}?latlng={lat},{long}&result_type=street_address&key={GOOGLE_MAP_TOKEN}"
    response = requests.get(url)
    data = response.json()
    return data["results"][0]["formatted_address"]

def build_row(data: dict, lat: float, long: float) -> dict:
    data["latitude"] = lat
    data["longitude"] = long
    return data

def test_geocode():
      
    url = "https://www.onemap.gov.sg/api/public/revgeocode?location=1.323290,103.900233&buffer=40&addressType=All&otherFeatures=N"
        
    headers = {"Authorization": ONEMAP_TOKEN}
        
    response = requests.request("GET", url, headers=headers)
      
    print(response.text)

def random_walk(lat: float, long: float, distance_meters: float):
    # 1 degree latitude is approximately 111 km
    # 1 degree longitude is approximately 111 km * cos(latitude)
    # 1 km is approximately 1/111
    # 1 m is approximately 1/111000
    walk_vector = np.random.uniform(-1, 1, 2)
    walk_vector /= np.linalg.norm(walk_vector)
    walk_vector *= distance_meters / 111000
    return lat + walk_vector[0], long + walk_vector[1]

def get_next_postal_code_address_pair(lat: float, long: float):
    new_lat, new_long = random_walk(lat, long, 100)
    address = reverse_geocode_google(new_lat, new_long)
    contains_postal_code = False
    postal_code = ""
    postal_code_regex = re.compile(r"\d{6}")
    # regex to extract postal code
    postal_code_match = postal_code_regex.search(address)
    if postal_code_match:
        postal_code = postal_code_match.group()
        contains_postal_code = True
    else:
        return get_next_postal_code_address_pair(new_lat, new_long)
    return new_lat, new_long, postal_code, address, contains_postal_code

if __name__ == "__main__":
    # Seed data
    RES = []
    for seed in SEED_ADDRESSES[:1]:
        row = build_row(seed, *get_lat_long(seed["address"]))
        RES.append(row)
        print(f"Address: {row['address']}, Latitude: {row['latitude']}, Longitude: {row['longitude']}")

        for _ in range(5):
            lat, long = random_walk(float(row["latitude"]), float(row["longitude"]), 100)
            print(get_next_postal_code_address_pair(lat, long))
    # test_geocode()