export const RANDOM_IMAGES = [
  'https://picsum.photos/1200/400?random=1',
  'https://picsum.photos/1200/400?random=2',
  'https://picsum.photos/1200/400?random=3',
  'https://picsum.photos/1200/400?random=4',
  'https://picsum.photos/1200/400?random=5',
  'https://picsum.photos/1200/400?random=6',
  'https://picsum.photos/1200/400?random=7',
  'https://picsum.photos/1200/400?random=8',
  'https://picsum.photos/1200/400?random=9',
  'https://picsum.photos/1200/400?random=10',
  'https://picsum.photos/1200/400?random=11',
  'https://picsum.photos/1200/400?random=12',
  'https://picsum.photos/1200/400?random=13',
  'https://picsum.photos/1200/400?random=14',
  'https://picsum.photos/1200/400?random=15',
  'https://picsum.photos/1200/400?random=16',
  'https://picsum.photos/1200/400?random=17',
  'https://picsum.photos/1200/400?random=18',
  'https://picsum.photos/1200/400?random=19',
  'https://picsum.photos/1200/400?random=20',
  'https://picsum.photos/1200/400?random=21',
  'https://picsum.photos/1200/400?random=22',
  'https://picsum.photos/1200/400?random=23',
  'https://picsum.photos/1200/400?random=24',
  'https://picsum.photos/1200/400?random=25',
  'https://picsum.photos/1200/400?random=26',
  'https://picsum.photos/1200/400?random=27',
  'https://picsum.photos/1200/400?random=28',
  'https://picsum.photos/1200/400?random=29',
  'https://picsum.photos/1200/400?random=30',
]

export function getRandomImageForEvent(uid) {
  if (!uid) return RANDOM_IMAGES[0]
  let hash = 0
  for (let i = 0; i < uid.length; i++) {
    hash = uid.charCodeAt(i) + ((hash << 5) - hash)
  }
  const index = Math.abs(hash) % RANDOM_IMAGES.length
  return RANDOM_IMAGES[index]
}
