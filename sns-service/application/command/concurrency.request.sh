#!/bin/bash

curl -L -v -d '{
                 "memberId": 1823,
                 "title": "title",
                 "contents": "contents",
                 "writer": "writer",
                 "images": [
                   {
                     "url": "image1",
                     "sortOrder": 0
                   },
                   {
                     "url": "image2",
                     "sortOrder": 1
                   }
                 ],
                 "tagType": "style",
                 "tagValues": [
                   "출근",
                   "여행"
                 ],
                 "productIds": [1, 2, 3, 4, 5]
               }' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/api/v1/sns &
curl -L -v -d '{
                 "memberId": 1823,
                 "title": "title",
                 "contents": "contents",
                 "writer": "writer",
                 "images": [
                   {
                     "url": "image1",
                     "sortOrder": 0
                   },
                   {
                     "url": "image2",
                     "sortOrder": 1
                   }
                 ],
                 "tagType": "style",
                 "tagValues": [
                   "출근",
                   "여행"
                 ],
                 "productIds": [1, 2, 3, 4, 5]
               }' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/api/v1/sns &
curl -L -v -d '{
                 "memberId": 1823,
                 "title": "title",
                 "contents": "contents",
                 "writer": "writer",
                 "images": [
                   {
                     "url": "image1",
                     "sortOrder": 0
                   },
                   {
                     "url": "image2",
                     "sortOrder": 1
                   }
                 ],
                 "tagType": "style",
                 "tagValues": [
                   "출근",
                   "여행"
                 ],
                 "productIds": [1, 2, 3, 4, 5]
               }' \
  -H "Accept: application/json" \
  -H "Content-Type: application/json" \
  --url http://localhost:9000/api/v1/sns
